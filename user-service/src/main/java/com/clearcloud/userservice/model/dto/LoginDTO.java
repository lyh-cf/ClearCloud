package com.clearcloud.userservice.model.dto;

/*
 *@title LoginDTO
 *@description
 *@author LYH
 *@version 1.0
 *@create 2023/10/29 20:01
 */

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class LoginDTO implements Serializable {
     @NotNull(message = "邮箱不能为空")
     @NotBlank(message = "邮箱不能为空")
     private String userEmail;
     @NotNull(message = "密码不能为空")
     @NotBlank(message = "密码不能为空")
     private String passWord;
}
