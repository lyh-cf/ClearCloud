package com.clearcloud.userservice.model.vo;

import lombok.Data;

import java.io.Serializable;

/*
 *@title BasicUserInfoVO
 *@description
 *@author LYH
 *@version 1.0
 *@create 2023/11/3 20:09
 */
@Data
public class BasicUserInfoVO implements Serializable {
    private Integer pkUserId;
    private String nickName;
    private String avatar;
    private String signature;
}
