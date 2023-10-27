package com.clearcloud.gateway.exception;

import com.clearcloud.base.model.StatusCodeEnum;

/*
 *@title MyException
 *@description
 *@author LYH
 *@version 1.0
 *@create 2023/10/27 9:40
 */
public class MyException extends RuntimeException{

    private final StatusCodeEnum statusCodeEnum;

    public MyException(StatusCodeEnum statusCodeEnum) {
        super(statusCodeEnum.getMessage());
        this.statusCodeEnum = statusCodeEnum;
    }

    public StatusCodeEnum getStatusCodeEnum() {
        return statusCodeEnum;
    }
}
