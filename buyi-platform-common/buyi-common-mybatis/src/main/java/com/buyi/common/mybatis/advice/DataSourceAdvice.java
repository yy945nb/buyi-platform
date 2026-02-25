package com.buyi.common.mybatis.advice;

import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.creator.DataSourceProperty;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.buyi.common.api.openfign.tenant.ITenantInfoServiceFeign;
import com.buyi.common.api.vo.TenantInfoVo;
import com.buyi.common.core.constant.BaseConstant;
import com.buyi.common.core.result.CommonResult;
import com.buyi.common.mybatis.annotation.DyDataSource;
import com.buyi.common.mybatis.util.*;
import com.buyi.common.oauth.util.AccessTokenUtils;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

/**
 *
 */
@Aspect
@Component
@RequiredArgsConstructor
public class DataSourceAdvice {

    @Autowired
    private DataSource dataSource;

    private final ITenantInfoServiceFeign tenantInfoServiceFeign;

    private final DynamicDataSourceUtil dynamicDataSourceUtil;

    private final RedisTemplate<Object, Object> redisTemplate;

    private final SpelUtil spelUtil;


    @Pointcut("@annotation(com.buyi.common.mybatis.annotation.DyDataSource)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object pointCutAround(ProceedingJoinPoint proceedingJoinPoint) {
        // 获取被代理的方法的参数
        Object[] args = proceedingJoinPoint.getArgs();
        // 获取被代理的对象
        Object target = proceedingJoinPoint.getTarget();
        // 获取通知签名
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        try {
            // 获取数据库连接信息
            DynamicRoutingDataSource dynamicRoutingDataSource = (DynamicRoutingDataSource) dataSource;
            String url = "";
            // 获取被代理的方法
            Method method = target.getClass().getMethod(signature.getName(), signature.getParameterTypes());
            DS ds = target.getClass().getAnnotation(DS.class);
            // 根据注解获取数据源
            if (ds != null) {
                String value = ds.value();
                DataSource dataSource1 = dynamicRoutingDataSource.getDataSource(value);
                url = dataSource1.getConnection().getMetaData().getURL();
            } else {
                Connection connection = dynamicRoutingDataSource.getConnection();
                DatabaseMetaData metaData = connection.getMetaData();
                url = metaData.getURL();
            }
            String dataBaseName = TableNameBuildUtil.getDataBaseName(url);
            // 获取方法上的注解
            DyDataSource annotation = method.getAnnotation(DyDataSource.class);
            // 不进行数据源切换操作
            if (annotation.self()) {
                return proceedingJoinPoint.proceed();
            }
            if (!annotation.autoTenantId()) {
                TenantHelper.set(false);
            }
            String methodTenantId = StrUtil.isNotBlank(annotation.tenantId()) ? spelUtil.generateKeyBySpEL(annotation.tenantId(), proceedingJoinPoint) : "";
            String tencentId = "";
            if (StrUtil.isNotBlank(methodTenantId)) {
                tencentId = methodTenantId;
            } else {
                // 从header中获取tenantId
                tencentId = !StrUtil.isNotBlank(AccessTokenUtils.getTenantId()) ? "" : AccessTokenUtils.getTenantId();
            }
            if (StrUtil.isNotBlank(tencentId)) {
                TenantInfoVo data = (TenantInfoVo) redisTemplate.opsForValue().get("my:tenant:" + tencentId);
                TenantInfoHelper.set(data);
                if (data != null) {
                    if (BaseConstant.DATASOURCE_TYPE_DB.equals(data.getDatasourceType())) {
                        String dynamicDataSource = dataBaseName + BaseConstant.UNDERLINE + tencentId;
                        if (!dynamicDataSourceUtil.existsDataSource(dynamicDataSource)) {
                            initDataSource(dynamicDataSource);
                        }
                        if (dynamicDataSourceUtil.existsDataSource(dynamicDataSource) && StrUtil.isNotBlank(dataBaseName)) {
                            DynamicDataSourceContextHolder.push(dynamicDataSource);
                        }
                    }
                } else {
                    // 查询租户的类型,分库的话需要在切换数据源时拼接 tenantId
                    CommonResult<TenantInfoVo> byTenantIdFeign = tenantInfoServiceFeign.getByTenantIdFeign(tencentId);
                    if (byTenantIdFeign.getCode() == 200) {
                        data = byTenantIdFeign.getData();
                        if (data != null && BaseConstant.DATASOURCE_TYPE_DB.equals(data.getDatasourceType())) {
                            String dynamicDataSource = dataBaseName + BaseConstant.UNDERLINE + tencentId;
                            if (!dynamicDataSourceUtil.existsDataSource(dynamicDataSource)) {
                                initDataSource(dynamicDataSource);
                            }
                            if (dynamicDataSourceUtil.existsDataSource(dynamicDataSource) && StrUtil.isNotBlank(dataBaseName)) {
                                DynamicDataSourceContextHolder.push(dynamicDataSource);
                            }
                        }
                    }
                }
            }
            return proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            DynamicDataSourceContextHolder.poll();
        }
    }

    private void initDataSource(String dynamicDataSource) {
        DataSourceProperty dataSourceProperty = (DataSourceProperty) redisTemplate.opsForValue().get("my:dynamicDataSource" + dynamicDataSource);
        if (dataSourceProperty != null) {
            dynamicDataSourceUtil.addDataSource(dataSourceProperty);
        }
    }
}
