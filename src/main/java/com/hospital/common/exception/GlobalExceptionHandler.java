package com.hospital.common.exception;

import cn.dev33.satoken.exception.NotLoginException;
import com.hospital.common.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseResult<?> handleBusinessException(BusinessException e) {
        log.warn("业务异常: {}", e.getMessage());
        ResponseCode code = e.getResponseCode();
        return ResponseResult.error(code.getCode(), code.getMessage());
    }

    @ExceptionHandler(NotLoginException.class)
    public ResponseResult<?> handleNotLoginException(NotLoginException e) {
        log.warn("未登录: {}", e.getMessage());
        return ResponseResult.error(ResponseCode.UNAUTHORIZED.getCode(), ResponseCode.UNAUTHORIZED.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResult<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("参数校验失败");
        log.warn("参数校验失败: {}", message);
        return ResponseResult.error(ResponseCode.PARAM_ERROR.getCode(), message);
    }

    @ExceptionHandler(Exception.class)
    public ResponseResult<?> handleException(Exception e) {
        log.error("系统异常", e);
        return ResponseResult.error(500, "服务器内部错误");
    }
}
