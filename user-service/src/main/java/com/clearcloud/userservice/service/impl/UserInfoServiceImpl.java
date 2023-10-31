package com.clearcloud.userservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clearcloud.userservice.mapper.UserInfoMapper;
import com.clearcloud.userservice.pojo.UserInfo;
import com.clearcloud.userservice.service.IUserInfoService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lyh
 * @since 2023-10-30
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {
    @Override
    public UserInfo getUserInfo(String userEmail) {
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getUserEmail, userEmail);
        return getOne(queryWrapper);
    }
    @Override
    public boolean checkUserEmailIsExist(String userEmail) {
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        //防止重复注册
        queryWrapper.eq(UserInfo::getUserEmail, userEmail);
        UserInfo result = getOne(queryWrapper);
        return result != null;
    }
}
