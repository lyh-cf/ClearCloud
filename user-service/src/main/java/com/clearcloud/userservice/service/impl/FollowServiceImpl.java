package com.clearcloud.userservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clearcloud.base.model.RedisConstants;
import com.clearcloud.userservice.mapper.FollowMapper;
import com.clearcloud.userservice.model.pojo.Follow;
import com.clearcloud.userservice.model.pojo.UserCount;
import com.clearcloud.userservice.service.IFollowService;
import com.clearcloud.userservice.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lyh
 * @since 2023-11-03
 */
@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements IFollowService {
     @Autowired
     private RedisUtil redisUtil;
     @Transactional
     public void follow(Integer userId,Integer targetUserId){
          //先写数据库
          Follow follow=new Follow();
          follow.setUserId(userId);
          follow.setFollowId(targetUserId);
          save(follow);
          //再写缓存
          redisUtil.sSet(RedisConstants.FOLLOW_KEY_PREFIX+userId,targetUserId);
          redisUtil.sSet(RedisConstants.FAN_KEY_PREFIX+targetUserId,userId);
          //修改用户统计信息
          UserCount targetUserCount =(UserCount) redisUtil.get(RedisConstants.USER_COUNT_KEY_PREFIX + targetUserId);
          targetUserCount.setFanCount(targetUserCount.getFanCount()+1);
          redisUtil.set(RedisConstants.USER_COUNT_KEY_PREFIX + targetUserId,targetUserCount);
          UserCount userCount =(UserCount) redisUtil.get(RedisConstants.USER_COUNT_KEY_PREFIX + userId);
          userCount.setFollowCount(userCount.getFollowCount()+1);
          redisUtil.set(RedisConstants.USER_COUNT_KEY_PREFIX+userId,userCount);
     }
     @Transactional
     public void unFollow(Integer userId,Integer targetUserId){
          //先写数据库
          LambdaUpdateWrapper<Follow> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
          lambdaUpdateWrapper.eq(Follow::getUserId, userId).eq(Follow::getFollowId,targetUserId);
          remove(lambdaUpdateWrapper);
          //再写缓存
          redisUtil.setRemove(RedisConstants.FOLLOW_KEY_PREFIX+userId,targetUserId);
          redisUtil.setRemove(RedisConstants.FAN_KEY_PREFIX+targetUserId,userId);
          //修改用户统计信息
          UserCount targetUserCount =(UserCount) redisUtil.get(RedisConstants.USER_COUNT_KEY_PREFIX + targetUserId);
          targetUserCount.setFanCount(targetUserCount.getFanCount()-1);
          redisUtil.set(RedisConstants.USER_COUNT_KEY_PREFIX + targetUserId,targetUserCount);
          UserCount userCount =(UserCount) redisUtil.get(RedisConstants.USER_COUNT_KEY_PREFIX + userId);
          userCount.setFollowCount(userCount.getFollowCount()-1);
          redisUtil.set(RedisConstants.USER_COUNT_KEY_PREFIX+userId,userCount);
     }
}
