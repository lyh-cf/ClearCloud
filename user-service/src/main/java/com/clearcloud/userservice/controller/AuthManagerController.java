package com.clearcloud.userservice.controller;

import com.clearcloud.base.model.BaseResponse;
import com.clearcloud.base.model.RedisConstants;
import com.clearcloud.base.utils.JwtUtil;
import com.clearcloud.userservice.model.dto.LoginDTO;
import com.clearcloud.userservice.model.dto.RegisterDTO;
import com.clearcloud.userservice.mapstruct.UserMapstruct;
import com.clearcloud.userservice.model.pojo.UserInfo;
import com.clearcloud.userservice.service.AuthService;
import com.clearcloud.userservice.service.UserInfoService;
import com.clearcloud.userservice.utils.RedisUtil;
import com.clearcloud.userservice.utils.SM3Util;
import com.clearcloud.userservice.model.vo.LoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;


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
    private UserInfoService userInfoService;
    @Autowired
    private AuthService authService;
    @Autowired
    private RedisUtil redisUtil;
    @ApiOperation("登录认证")
    @PostMapping("/login")
    public BaseResponse<?> login(@Valid @RequestBody LoginDTO loginDTO) {
        UserInfo userInfo = userInfoService.getUserInfo(loginDTO.getUserEmail());
        if (userInfo == null) return BaseResponse.error("用户不存在");
        if (!userInfo.getPassWord().equals(SM3Util.encryptPassword(loginDTO.getPassWord())))return BaseResponse.error("密码错误");
        LoginVO loginVO= UserMapstruct.INSTANCT.conver(userInfo);
        loginVO.setAccess_token(JwtUtil.createAccessToken(userInfo.getPkUserId()));
        loginVO.setRefresh_token(JwtUtil.createRefreshToken(userInfo.getPkUserId()));
        /*
        缓存用户基本信息，用hash结构存储，key为：业务前缀+id，hash-key为用户的id，hash-value为user-info
        key为：业务前缀+id 可以防止big-key问题
        */
        redisUtil.set(RedisConstants.USER_INFO_KEY_PREFIX+userInfo.getPkUserId(),userInfo,RedisConstants.USER_INFORMATION_TTL);
        return BaseResponse.success(loginVO);
    }
    @PostMapping("/register")
    @ApiOperation("注册接口")
    public BaseResponse<?>register(@Valid @RequestBody RegisterDTO registerDTO) {
        if (userInfoService.checkUserEmailIsExist(registerDTO.getUserEmail())) return BaseResponse.error("邮箱已注册");
        registerDTO.setPassWord(SM3Util.encryptPassword(registerDTO.getPassWord()));
        authService.registerUserInformation(registerDTO);
        return BaseResponse.success();
    }

}
