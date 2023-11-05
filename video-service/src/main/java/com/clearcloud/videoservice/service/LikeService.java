package com.clearcloud.videoservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clearcloud.base.model.RedisConstants;
import com.clearcloud.api.UserServiceClient;
import com.clearcloud.videoservice.mapper.LikeMapper;
import com.clearcloud.videoservice.model.pojo.Like;
import com.clearcloud.videoservice.model.pojo.VideoCount;
import com.clearcloud.videoservice.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lyh
 * @since 2023-11-02
 */
@Service
public class LikeService extends ServiceImpl<LikeMapper, Like> implements IService<Like> {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserServiceClient userServiceClient;

    @Transactional
    public void likeVideo(Integer userId, Integer videoId) {
        //todo 写回策略
        redisUtil.sSet(RedisConstants.LIKE_KEY_PREFIX + userId, videoId);
        VideoCount videoCount=(VideoCount) redisUtil.get(RedisConstants.VIDEO_COUNT_KEY_PREFIX+videoId);
        videoCount.setLikedCount(videoCount.getLikedCount()+1);
        redisUtil.set(RedisConstants.VIDEO_COUNT_KEY_PREFIX+videoId,videoCount);
        userServiceClient.addLikedCount(userId);
    }

    @Transactional
    public void cancelLikeVideo(Integer userId, Integer videoId) {
        //todo 写回策略
        redisUtil.setRemove(RedisConstants.LIKE_KEY_PREFIX + userId, videoId);
        VideoCount videoCount=(VideoCount) redisUtil.get(RedisConstants.VIDEO_COUNT_KEY_PREFIX+videoId);
        videoCount.setLikedCount(videoCount.getLikedCount()-1);
        redisUtil.set(RedisConstants.VIDEO_COUNT_KEY_PREFIX+videoId,videoCount);
        userServiceClient.reduceLikedCount(userId);
    }

}
