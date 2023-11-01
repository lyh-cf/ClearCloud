package com.clearcloud.userservice.mapstruct;

import com.clearcloud.userservice.dto.RegisterDTO;
import com.clearcloud.userservice.dto.UserSelfInfoDTO;
import com.clearcloud.userservice.pojo.UserCount;
import com.clearcloud.userservice.pojo.UserInfo;
import com.clearcloud.userservice.vo.LoginVO;
import com.clearcloud.userservice.vo.UserInformationVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/*
 *@title UserInfoMapstruct
 *@description
 *@author LYH
 *@version 1.0
 *@create 2023/10/30 21:46
 */
@Mapper
public interface UserMapstruct {
    UserMapstruct INSTANCT = Mappers.getMapper(UserMapstruct.class);

    //返回的是目标对象
    LoginVO conver(UserInfo userInfo);

    UserInfo conver(RegisterDTO registerDTO);

    @Mappings({
            @Mapping(source = "userInfo.pkUserId", target = "pkUserId"),
    })
    UserInformationVO conver(UserInfo userInfo, UserCount userCount);

    UserInfo conver(UserSelfInfoDTO userSelfInfoDTO);
}
