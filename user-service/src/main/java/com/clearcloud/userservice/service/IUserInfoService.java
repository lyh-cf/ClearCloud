package com.clearcloud.userservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clearcloud.userservice.pojo.UserInfo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lyh
 * @since 2023-10-30
 */
public interface IUserInfoService extends IService<UserInfo> {
     UserInfo getUserInfo(String userEmail);

     boolean checkUserEmailIsExist(String userEmail);
}