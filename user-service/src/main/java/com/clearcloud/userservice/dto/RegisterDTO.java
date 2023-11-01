package com.clearcloud.userservice.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/*
 *@title RegisterDTO
 *@description
 *@author LYH
 *@version 1.0
 *@create 2023/10/30 21:50
 */
@Data
public class RegisterDTO implements Serializable {
    @NotNull(message = "邮箱不能为空")
    @NotBlank(message = "邮箱不能为空")
    private String userEmail;
    @NotNull(message = "密码不能为空")
    @NotBlank(message = "密码不能为空")
    private String passWord;
}
