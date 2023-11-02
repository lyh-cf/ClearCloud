package com.clearcloud.userservice.model.dto;

import lombok.Data;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/*
 *@title UserSelfInfoDTO
 *@description
 *@author LYH
 *@version 1.0
 *@create 2023/11/1 19:23
 */
@Data
public class UserSelfInfoDTO implements Serializable {
    @NotNull(message = "用户id不能为空")
    private Integer pkUserId;
    private String userEmail;
    private String nickName;
    private String avatar;
    private String signature;
}
