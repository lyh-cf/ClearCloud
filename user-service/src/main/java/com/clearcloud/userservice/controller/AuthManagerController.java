package com.clearcloud.userservice.controller;

import com.clearcloud.base.model.BaseResponse;
import com.clearcloud.userservice.dto.LoginDTO;
import com.clearcloud.userservice.dto.RegisterDTO;
import com.clearcloud.userservice.mapstruct.UserInfoMapstruct;
import com.clearcloud.userservice.pojo.UserInfo;
import com.clearcloud.userservice.service.impl.UserInfoServiceImpl;
import com.clearcloud.userservice.utils.JwtUtil;
import com.clearcloud.userservice.utils.SM3Util;
import com.clearcloud.userservice.vo.LoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;


/*
 *@title AuthManagerController
 *@description
 *@author LYH
 *@version 1.0
 *@create 2023/10/29 19:40
 */
@RestController
@Slf4j
@Api(tags = "认证接口")
public class AuthManagerController {
    @Autowired
    private UserInfoServiceImpl userInfoService;

    @ApiOperation("登录认证")
    @PostMapping("/login")
    public BaseResponse<?> login(@Valid @RequestBody LoginDTO loginDTO) {
        UserInfo userInfo = userInfoService.getUserInfo(loginDTO.getUserEmail());
        if (userInfo == null) return BaseResponse.error("用户不存在");
        if (!userInfo.getPassWord().equals(SM3Util.encryptPassword(loginDTO.getPassWord())))return BaseResponse.error("密码错误");
        LoginVO loginVO= UserInfoMapstruct.INSTANCT.conver(userInfo);
        return BaseResponse.success(loginVO);
    }
    @PostMapping("/register")
    @ApiOperation("注册接口")
    public BaseResponse<?>register(@Valid @RequestBody RegisterDTO registerDTO) {
        if (userInfoService.checkUserEmailIsExist(registerDTO.getUserEmail())) return BaseResponse.error("邮箱已注册");
        registerDTO.setPassWord(SM3Util.encryptPassword(registerDTO.getPassWord()));
        //对象转换
        UserInfo userInfo = UserInfoMapstruct.INSTANCT.conver(registerDTO);
        userInfoService.save(userInfo);
        return BaseResponse.success();
    }
}
