package com.clearcloud.videoservice.controller;

import com.clearcloud.base.model.BaseResponse;
import com.clearcloud.base.model.RedisConstants;
import com.clearcloud.base.utils.JwtUtil;
import com.clearcloud.videoservice.model.pojo.VideoCount;
import com.clearcloud.videoservice.model.pojo.VideoInfo;
import com.clearcloud.videoservice.service.impl.VideoCountServiceImpl;
import com.clearcloud.videoservice.service.impl.VideoInfoServiceImpl;
import com.clearcloud.videoservice.model.vo.UploadVideoVO;
import com.clearcloud.videoservice.utils.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/*
 *@title WorkController
 *@description
 *@author LYH
 *@version 1.0
 *@create 2023/11/1 22:49
 */
@RestController
@Api(tags = "创作中心接口")
public class WorkController {
    @Autowired
    private VideoInfoServiceImpl videoInfoService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private VideoCountServiceImpl videoCountService;

    @ApiOperation("视频上传接口")
    @PostMapping("/uploadVideo")
    public BaseResponse<?> uploadVideo(HttpServletRequest httpServletRequest, @RequestParam("video") MultipartFile video) {
        Integer userId = JwtUtil.getUserId(httpServletRequest);
        UploadVideoVO uploadVideoVO = videoInfoService.uploadVideo(video, userId);
        return BaseResponse.success(uploadVideoVO);
    }
    @ApiOperation("发布作品接口")
    @PostMapping("/publishWork")
    public BaseResponse<?> publishWork(@RequestBody VideoInfo videoInfo) {
        //用事务保证操作的原子性
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                try {
                    // 先存数据库
                    videoInfoService.save(videoInfo);
                    VideoCount videoCount=new VideoCount();
                    videoCount.setPkVideoId(videoInfo.getPkVideoId());
                    videoCountService.save(videoCount);
                    //再存缓存
                    redisTemplate.opsForZSet().add(RedisConstants.ALL_PUBLISHED_VIDEO_KEY,videoInfo.getPkVideoId(),System.currentTimeMillis());
                    //todo
                    redisTemplate.opsForValue().set(RedisConstants.VIDEO_COUNT_KEY_PREFIX+videoCount.getPkVideoId(), videoCount);
                } catch (Exception e){
                    //回滚
                    transactionStatus.setRollbackOnly();
                }
            }
        });
        return BaseResponse.success();
    }
}
