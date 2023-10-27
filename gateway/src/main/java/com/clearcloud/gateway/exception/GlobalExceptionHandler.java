package com.clearcloud.gateway.exception;


import com.clearcloud.base.model.BaseResponse;
import com.clearcloud.base.model.StatusCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
/*
 *@title MyException
 *@description
 *@author LYH
 *@version 1.0
 *@create 2023/10/27 9:47
 */

/**
 * @ControllerAdvice
 * 1.全局异常处理
 * 2.全局数据绑定
 * 3.全局数据预处理
 */
@RestControllerAdvice//增强的 Controller
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MyException.class)
    public BaseResponse<?> myExceptionHandler(MyException e) {
        log.error("MyException: " + e.getMessage(), e);
        return BaseResponse.error(e.getStatusCodeEnum(), e.getMessage());
    }


    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("MyException: ", e);
        return BaseResponse.error(StatusCodeEnum.SYSTEM_ERROR.getCode(), e.getMessage());
    }
}
