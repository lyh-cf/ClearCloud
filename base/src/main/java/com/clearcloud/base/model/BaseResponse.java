package com.clearcloud.base.model;

import lombok.Data;

import java.io.Serializable;

/*
 *@title BaseResponse
 *@description
 *@author LYH
 *@version 1.0
 *@create 2023/10/27 8:42
 */
@Data
public class BaseResponse<T> implements Serializable {
    /**
     * 状态码
     */
    private int code;//编码：1成功，0和其它数字为失败

    /**
     * 数据
     */
    private T data;

    /**
     * 消息
     */
    private String message;

    //把构造方法私有
    private BaseResponse() {
    }

    public static <T> BaseResponse<T> success() {
        BaseResponse<T> response = new BaseResponse<T>();
        response.code = StatusCodeEnum.SUCCESS.getCode();
        response.message = StatusCodeEnum.SUCCESS.getMessage();
        return response;
    }

    public static <T> BaseResponse<T> error() {
        BaseResponse<T> response = new BaseResponse<T>();
        response.code = StatusCodeEnum.FAIL.getCode();
        response.message = StatusCodeEnum.FAIL.getMessage();
        return response;
    }

    public static <T> BaseResponse<T> success(T object) {
        BaseResponse<T> response = success();
        response.data = object;
        return response;
    }
    public static <T> BaseResponse<T> error(String message) {
        BaseResponse<T> response = error();
        response.setMessage(message);
        return response;
    }
    //todo 只限全局异常处理器、网管使用
    public static <T> BaseResponse<T> error(int code,String message) {
        BaseResponse<T> response = new BaseResponse<T>();
        response.code = code;
        response.message = message;
        return response;
    }

    public static <T> BaseResponse<T> error(StatusCodeEnum statusCodeEnum) {
        BaseResponse<T> response = new BaseResponse<T>();
        response.code = statusCodeEnum.getCode();
        response.message = statusCodeEnum.getMessage();
        return response;
    }

    public static <T> BaseResponse<T> error(StatusCodeEnum statusCodeEnum, T object) {
        BaseResponse<T> response = error(statusCodeEnum);
        response.data = object;
        return response;
    }

    public static BaseResponse<Object> judge(boolean save) {
        return save ? success() : error();
    }
}
