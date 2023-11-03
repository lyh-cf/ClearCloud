package com.clearcloud.videoservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.clearcloud.api.UserService;
import com.clearcloud.base.model.RedisConstants;
import com.clearcloud.videoservice.mapper.LikeMapper;
import com.clearcloud.videoservice.model.pojo.Like;
import com.clearcloud.videoservice.utils.RedisUtil;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lyh
 * @since 2023-11-02
 */
@Service
public class LikeService extends ServiceImpl<LikeMapper, Like> implements IService<Like> {
      @Autowired
      private RedisUtil redisUtil;
      @DubboReference
      private UserService userService;
      @Transactional
      public void likeVideo(Integer userId,Integer videoId){
           //todo 写回策略
           redisUtil.sSet(RedisConstants.LIKE_KEY_PREFIX+userId,videoId);
           userService.addLikedCount(userId);
      }
      @Transactional
      public void cancelLikeVideo(Integer userId,Integer videoId){
           //todo 写回策略
          redisUtil.setRemove(RedisConstants.LIKE_KEY_PREFIX+userId,videoId);
          userService.reduceLikedCount(userId);
      }
}
