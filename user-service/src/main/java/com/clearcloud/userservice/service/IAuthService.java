package com.clearcloud.userservice.service;

import com.clearcloud.userservice.model.dto.RegisterDTO;

/*
 *@title IAuthService
 *@description
 *@author LYH
 *@version 1.0
 *@create 2023/10/31 23:53
 */
public interface IAuthService {
     void registerUserInformation(RegisterDTO registerDTO);
}
