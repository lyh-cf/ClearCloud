package com.clearcloud.base.model;

/*
 *@title BaseResponse
 *@description
 *@author LYH
 *@version 1.0
 *@create 2023/10/27 8:46
 */
public enum StatusCodeEnum {

    SUCCESS(0, "success"),
    FAIL(1,"fail"),
    PARAMS_ERROR(400, "请求参数错误"),
    NULL_ERROR(400, "请求数据为空"),
    NOT_LOGIN(401, "用户未登录"),
    NO_AUTH(401, "无权限访问"),
    SYSTEM_ERROR(500, "系统内部异常");
    private final int code;
    /**
     * 状态码信息
     */
    private final String message;

    StatusCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
