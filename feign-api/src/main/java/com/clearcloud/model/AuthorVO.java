package com.clearcloud.model;

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
    private boolean isFollow;//该用户是否关注这个作者
    private boolean isCollect;//该用户是否收藏
    private boolean isLike;//该用户是否点赞
}
