package com.clearcloud.userservice.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/*
 *@title LoginVO
 *@description
 *@author LYH
 *@version 1.0
 *@create 2023/10/30 21:35
 */
@Data
public class LoginVO implements Serializable {
    private Integer pkUserId;
    private String userEmail;
    private String nickName;
    private String avatar;
    private String signature;
    @ApiModelProperty(value = "access_token")
    private String access_token;
    @ApiModelProperty(value = "refresh_token")
    private String refresh_token;
}
