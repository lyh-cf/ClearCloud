package com.clearcloud.userservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clearcloud.api.UserService;
import com.clearcloud.base.model.RedisConstants;
import com.clearcloud.userservice.mapper.UserCountMapper;
import com.clearcloud.userservice.model.pojo.UserCount;
import com.clearcloud.userservice.utils.RedisUtil;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lyh
 * @since 2023-10-30
 */
@DubboService
public class UserCountService extends ServiceImpl<UserCountMapper, UserCount> implements IService<UserCount>, UserService {
    @Resource
    private RedisUtil redisUtil;
    @Override
    public void addLikedCount(Integer userId) {
         UserCount userCount=(UserCount) redisUtil.get(RedisConstants.USER_COUNT_KEY_PREFIX+userId);
         userCount.setLikedCount(userCount.getLikedCount()+1);
         redisUtil.set(RedisConstants.USER_COUNT_KEY_PREFIX + userId,userCount);
    }

    @Override
    public void reduceLikedCount(Integer userId) {
        UserCount userCount=(UserCount) redisUtil.get(RedisConstants.USER_COUNT_KEY_PREFIX+userId);
        userCount.setLikedCount(userCount.getLikedCount()-1);
        redisUtil.set(RedisConstants.USER_COUNT_KEY_PREFIX + userId,userCount);
    }

    @Override
    public void collectVideo(Integer userId, Integer videoId) {

    }

    @Override
    public void cancelCollectVideo(Integer userId, Integer videoId) {

    }

}
