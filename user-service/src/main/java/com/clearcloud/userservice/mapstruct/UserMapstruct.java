package com.clearcloud.userservice.mapstruct;

import com.clearcloud.model.AuthorVO;
import com.clearcloud.userservice.model.dto.RegisterDTO;
import com.clearcloud.userservice.model.dto.UserSelfInfoDTO;
import com.clearcloud.userservice.model.pojo.UserCount;
import com.clearcloud.userservice.model.pojo.UserInfo;
import com.clearcloud.userservice.model.vo.BasicUserInfoVO;
import com.clearcloud.userservice.model.vo.LoginVO;
import com.clearcloud.userservice.model.vo.UserInformationVO;
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

    BasicUserInfoVO converToBasicUserInfoVO(UserInfo userInfo);

    AuthorVO converToAuthorVO(UserInfo userInfo);
}
