package com.clearcloud.userservice.service.impl;

import com.clearcloud.userservice.model.dto.RegisterDTO;
import com.clearcloud.userservice.mapstruct.UserMapstruct;
import com.clearcloud.userservice.model.pojo.UserCount;
import com.clearcloud.userservice.model.pojo.UserInfo;
import com.clearcloud.userservice.service.IAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
 *@title IAuthServiceImpl
 *@description
 *@author LYH
 *@version 1.0
 *@create 2023/10/31 23:54
 */
@Service
@Slf4j
public class IAuthServiceImpl implements IAuthService {
    @Autowired
    private UserInfoServiceImpl userInfoService;
    @Autowired
    private UserCountServiceImpl userCountMapper;
    @Transactional
    @Override
    public void registerUserInformation(RegisterDTO registerDTO) {
        //对象转换
        UserInfo userInfo = UserMapstruct.INSTANCT.conver(registerDTO);
        userInfoService.save(userInfo);//userInfo会保存插入的userId
        UserCount userCount=new UserCount();
        userCount.setPkUserId(userInfo.getPkUserId());
        userCountMapper.save(userCount);
    }
}
