package com.clearcloud.videoservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.clearcloud.api.UserServiceClient;
import com.clearcloud.base.model.RedisConstants;
import com.clearcloud.videoservice.mapper.VideoCountMapper;
import com.clearcloud.videoservice.model.pojo.VideoCount;
import com.clearcloud.videoservice.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lyh
 * @since 2023-11-02
 */
@Service
public class VideoCountService extends ServiceImpl<VideoCountMapper, VideoCount> implements IService<VideoCount> {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserServiceClient userService;
    @Transactional
    public void collectVideo(Integer userId,Integer videoId){
        VideoCount videoCount=(VideoCount) redisUtil.get(RedisConstants.VIDEO_COUNT_KEY_PREFIX+videoId);
        videoCount.setCollectedCount(videoCount.getCollectedCount()+1);
        redisUtil.set(RedisConstants.VIDEO_COUNT_KEY_PREFIX+videoId,videoCount);
//        //注册一个事务同步器，这个事务同步器，可以允许在事务提交后，做一些事情
//        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
//            @Override
//            public void afterCommit() {
//                userService.collectVideo(userId,videoId);
//            }
//        });
        userService.collectVideo(userId,videoId);
    }
    @Transactional
    public void cancelCollectVideo(Integer userId,Integer videoId){
        VideoCount videoCount=(VideoCount) redisUtil.get(RedisConstants.VIDEO_COUNT_KEY_PREFIX+videoId);
        videoCount.setCollectedCount(videoCount.getCollectedCount()-1);
        redisUtil.set(RedisConstants.VIDEO_COUNT_KEY_PREFIX+videoId,videoCount);
//        //注册一个事务同步器，这个事务同步器，可以允许在事务提交后，做一些事情
//        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
//            @Override
//            public void afterCommit() {
//                userService.cancelCollectVideo(userId,videoId);
//            }
//        });
        userService.cancelCollectVideo(userId,videoId);
    }
}
