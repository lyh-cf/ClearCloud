package com.clearcloud.userservice.mapstruct;

import com.clearcloud.userservice.dto.RegisterDTO;
import com.clearcloud.userservice.pojo.UserInfo;
import com.clearcloud.userservice.vo.LoginVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/*
 *@title UserInfoMapstruct
 *@description
 *@author LYH
 *@version 1.0
 *@create 2023/10/30 21:46
 */
@Mapper
public interface UserInfoMapstruct {
    UserInfoMapstruct INSTANCT = Mappers.getMapper(UserInfoMapstruct.class);

    //返回的是目标对象
    LoginVO conver(UserInfo userInfo);

    UserInfo conver(RegisterDTO registerDTO);
}
