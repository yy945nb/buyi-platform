package com.buyi.framework.log.diff.support.aop;

import com.buyi.framework.log.diff.core.context.DiffLogContext;
import com.buyi.framework.log.diff.domain.DiffLogInfo;
import com.buyi.framework.log.diff.domain.DiffLogOps;
import com.buyi.framework.log.diff.domain.MethodExecute;
import com.buyi.framework.log.diff.service.IDiffLogPerformanceMonitor;
import com.buyi.framework.log.diff.service.IDiffLogService;
import com.buyi.framework.log.diff.support.parse.DiffLogValueParser;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * AOP interceptor that intercepts methods annotated with {@link com.buyi.framework.log.diff.core.annotation.DiffLog}
 * and records diff log entries.
 *
 * @author Levin
 */
@Slf4j
@Setter
public class DiffLogInterceptor extends DiffLogValueParser implements MethodInterceptor {

    private DiffLogOperationSource diffLogOperationSource;
    private String serviceName;
    private boolean joinTransaction;
    private Boolean diffLog;
    private IDiffLogPerformanceMonitor diffLogPerformanceMonitor;

    @Override
    public Object invoke(@NonNull MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        return execute(invocation, invocation.getThis(), method, invocation.getArguments());
    }

    private Object execute(MethodInvocation invoker, Object target, Method method, Object[] args) throws Throwable {
        if (target == null) {
            return invoker.proceed();
        }
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(target);
        Collection<DiffLogOps> operations = diffLogOperationSource.computeDiffLogOperations(method, targetClass);
        if (operations.isEmpty()) {
            return invoker.proceed();
        }

        StopWatch stopWatch = new StopWatch(IDiffLogPerformanceMonitor.MONITOR_NAME);
        DiffLogContext.putEmptySpan();
        boolean success = false;
        Object result = null;
        Throwable throwable = null;
        try {
            MethodExecute methodExecute = new MethodExecute(method, args, targetClass);

            stopWatch.start(IDiffLogPerformanceMonitor.MONITOR_TASK_BEFORE_EXECUTE);
            List<String> templates = collectTemplates(operations);
            Map<String, String> functionNameAndReturnValueMap =
                    processBeforeExecuteFunctionTemplate(templates, targetClass, method, args);
            stopWatch.stop();

            try {
                result = invoker.proceed();
                success = true;
            } catch (Throwable t) {
                throwable = t;
                throw t;
            } finally {
                methodExecute.setSuccess(success);
                methodExecute.setResult(result);
                if (throwable != null) {
                    methodExecute.setThrowable(throwable);
                    methodExecute.setErrorMsg(throwable.getMessage());
                }
                stopWatch.start(IDiffLogPerformanceMonitor.MONITOR_TASK_AFTER_EXECUTE);
                try {
                    recordAll(operations, methodExecute, functionNameAndReturnValueMap);
                } finally {
                    stopWatch.stop();
                    DiffLogContext.clear();
                    DiffLogContext.clearGlobal();
                    if (diffLogPerformanceMonitor != null) {
                        diffLogPerformanceMonitor.print(stopWatch);
                    }
                }
            }
        } catch (Throwable t) {
            if (t != throwable) {
                log.error("DiffLog process error", t);
            }
            throw t;
        }
        return result;
    }

    private List<String> collectTemplates(Collection<DiffLogOps> operations) {
        List<String> templates = new ArrayList<>();
        for (DiffLogOps ops : operations) {
            if (StringUtils.hasText(ops.getSuccessLogTemplate())) {
                templates.add(ops.getSuccessLogTemplate());
            }
            if (StringUtils.hasText(ops.getFailLogTemplate())) {
                templates.add(ops.getFailLogTemplate());
            }
            if (StringUtils.hasText(ops.getExtra())) {
                templates.add(ops.getExtra());
            }
            if (StringUtils.hasText(ops.getBusinessKey())) {
                templates.add(ops.getBusinessKey());
            }
        }
        return templates;
    }

    private void recordAll(Collection<DiffLogOps> operations, MethodExecute methodExecute,
                           Map<String, String> functionNameAndReturnValueMap) {
        for (DiffLogOps ops : operations) {
            try {
                record(ops, methodExecute, functionNameAndReturnValueMap);
            } catch (Exception e) {
                log.error("DiffLog record error", e);
            }
        }
    }

    private void record(DiffLogOps ops, MethodExecute methodExecute,
                        Map<String, String> functionNameAndReturnValueMap) {
        if (!resolveCondition(ops.getCondition(), methodExecute, functionNameAndReturnValueMap)) {
            return;
        }
        boolean isSuccess = isSuccess(ops, methodExecute, functionNameAndReturnValueMap);
        String logTemplate = isSuccess ? ops.getSuccessLogTemplate() : ops.getFailLogTemplate();
        if (!StringUtils.hasText(logTemplate)) {
            return;
        }

        String description = singleProcessTemplate(methodExecute, logTemplate, functionNameAndReturnValueMap);
        String extra = singleProcessTemplate(methodExecute, ops.getExtra(), functionNameAndReturnValueMap);
        String businessKey = singleProcessTemplate(methodExecute, ops.getBusinessKey(), functionNameAndReturnValueMap);

        if (Boolean.TRUE.equals(diffLog) && !StringUtils.hasText(description)) {
            return;
        }

        DiffLogInfo logInfo = DiffLogInfo.builder()
                .serviceName(serviceName)
                .businessGroup(ops.getGroup())
                .businessTag(ops.getTag())
                .businessKey(businessKey)
                .description(description)
                .status(isSuccess)
                .extra(extra)
                .createdTime(Instant.now())
                .variables(new java.util.HashMap<>(DiffLogContext.getVariables() != null ? DiffLogContext.getVariables() : java.util.Collections.emptyMap()))
                .build();

        try {
            if (beanFactory != null) {
                IDiffLogService service = beanFactory.getBean(IDiffLogService.class);
                service.handler(logInfo);
            }
        } catch (Exception e) {
            log.error("DiffLog service handler error", e);
        }
    }

    private boolean resolveCondition(String condition, MethodExecute methodExecute,
                                     Map<String, String> functionNameAndReturnValueMap) {
        if (!StringUtils.hasText(condition)) {
            return true;
        }
        String value = singleProcessTemplate(methodExecute, condition, functionNameAndReturnValueMap);
        return Boolean.parseBoolean(value);
    }

    private boolean isSuccess(DiffLogOps ops, MethodExecute methodExecute,
                              Map<String, String> functionNameAndReturnValueMap) {
        if (!methodExecute.isSuccess()) {
            return false;
        }
        if (!StringUtils.hasText(ops.getIsSuccess())) {
            return true;
        }
        String value = singleProcessTemplate(methodExecute, ops.getIsSuccess(), functionNameAndReturnValueMap);
        return Boolean.parseBoolean(value);
    }
}
