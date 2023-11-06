package com.clearcloud.userservice.exception;


import com.clearcloud.base.model.BaseResponse;
import com.clearcloud.base.model.StatusCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.BindException;
import java.util.List;
import java.util.stream.Collectors;
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
    @ResponseStatus(HttpStatus.BAD_REQUEST) //设置状态码为 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse<?> bindExceptionHandler(MethodArgumentNotValidException e) {
        BindingResult exceptions = e.getBindingResult();
        // 判断异常中是否有错误信息，如果存在就使用异常中的消息，否则使用默认消息
        if (exceptions.hasErrors()) {
            List<ObjectError> errors = exceptions.getAllErrors();
            if (!errors.isEmpty()) {
                // 这里列出了全部错误参数，按正常逻辑，只需要第一条错误即可
                FieldError fieldError = (FieldError) errors.get(0);
                return BaseResponse.error(fieldError.getDefaultMessage());
            }
        }
        //使用默认消息：请求参数错误
        return BaseResponse.error(StatusCodeEnum.PARAMS_ERROR.getMessage());
    }
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //设置状态码为 500
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("MyException: ", e);
        return BaseResponse.error(StatusCodeEnum.SYSTEM_ERROR.getCode(), StatusCodeEnum.SYSTEM_ERROR.getMessage());
    }
}
