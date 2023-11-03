package com.clearcloud.videoservice.model.vo;

import lombok.Data;

import java.io.Serializable;

/*
 *@title AuthorVO
 *@description
 *@author LYH
 *@version 1.0
 *@create 2023/11/3 1:28
 */
@Data
public class AuthorVO implements Serializable {
    private Integer pkUserId;//作者的id
    private String nickName;//作者的昵称
    private String avatar;//作者的头像
    private String signature;//作者的签名
    private boolean isFollow;//该用户是否关注这个作者
}
