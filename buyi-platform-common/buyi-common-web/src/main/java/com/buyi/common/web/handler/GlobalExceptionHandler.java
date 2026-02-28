package com.buyi.common.web.handler;

import com.buyi.common.core.exception.BizException;
import com.buyi.common.core.result.CommonResult;
import com.buyi.common.core.result.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局 Web 层异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BizException.class)
    @ResponseBody
    public CommonResult<?> handleBizException(BizException e) {
        log.warn("业务异常: {}", e.getMessage());
        if (e.getResultCode() != null) {
            return CommonResult.failed(e.getResultCode());
        }
        return CommonResult.failed(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public CommonResult<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getAllErrors().isEmpty()
                ? "参数校验失败"
                : e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return CommonResult.validateFailed(message);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public CommonResult<?> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("参数异常: {}", e.getMessage());
        return CommonResult.failed(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public CommonResult<?> handleException(Exception e, HttpServletRequest request) {
        log.error("[系统异常] - [{}]", request.getRequestURI(), e);
        return CommonResult.failed(ResultCode.FAILED);
    }
}
