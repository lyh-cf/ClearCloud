package com.clearcloud.videoservice.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.clearcloud.base.model.BaseResponse;
import com.clearcloud.base.model.RedisConstants;
import com.clearcloud.base.utils.JwtUtil;
import com.clearcloud.model.VideoStreamVO;
import com.clearcloud.videoservice.mapper.VideoInfoMapper;
import com.clearcloud.videoservice.mapstruct.VideoMapstruct;
import com.clearcloud.videoservice.model.pojo.VideoCount;
import com.clearcloud.videoservice.model.pojo.VideoInfo;
import com.clearcloud.videoservice.service.VideoCountService;
import com.clearcloud.videoservice.service.VideoInfoService;
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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    private VideoInfoService videoInfoService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private VideoCountService videoCountService;
    @Resource
    private VideoInfoMapper videoInfoMapper;
    @Autowired
    private RedisUtil redisUtil;

    @ApiOperation("视频上传接口")
    @PostMapping("/uploadVideo")
    public BaseResponse<?> uploadVideo(HttpServletRequest httpServletRequest, @RequestParam("video") MultipartFile video) {
        Integer userId = JwtUtil.getUserId(httpServletRequest);
        UploadVideoVO uploadVideoVO = videoInfoService.uploadVideo(video, userId);
        return BaseResponse.success(uploadVideoVO);
    }
    @ApiOperation("删除作品接口")
    @DeleteMapping("/deleteVideo")
    public BaseResponse<?> deleteVideo(@RequestParam("videoId")Integer videoId) {
        LambdaUpdateWrapper<VideoInfo> lambdaUpdateWrapper1 = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper1.eq(VideoInfo::getPkVideoId, videoId);
        videoInfoService.remove(lambdaUpdateWrapper1);

        LambdaUpdateWrapper<VideoCount> lambdaUpdateWrapper2 = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper2.eq(VideoCount::getPkVideoId, videoId);
        videoCountService.remove(lambdaUpdateWrapper2);
        redisUtil.del(RedisConstants.VIDEO_COUNT_KEY_PREFIX+videoId);
        return BaseResponse.success();
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
                    VideoCount videoCount = new VideoCount();
                    videoCount.setPkVideoId(videoInfo.getPkVideoId());
                    videoCount.setCommentedCount(0);
                    videoCount.setLikedCount(0);
                    videoCount.setCollectedCount(0);
                    videoCountService.save(videoCount);
                    //再存缓存
                    redisTemplate.opsForZSet().add(RedisConstants.ALL_PUBLISHED_VIDEO_KEY, videoInfo.getPkVideoId(), System.currentTimeMillis());
                    //todo
                    redisTemplate.opsForValue().set(RedisConstants.VIDEO_COUNT_KEY_PREFIX + videoCount.getPkVideoId(), videoCount);
                } catch (Exception e) {
                    e.printStackTrace();
                    //回滚
                    transactionStatus.setRollbackOnly();
                }
            }
        });
        return BaseResponse.success();
    }

    @ApiOperation("获取用户的作品接口,用于feign接口调用")
    @GetMapping("/getUserWorks")
    public List<VideoStreamVO> getUserWorks(@RequestParam("userId") Integer userId) {
        //批量查作品VideoInfo
        List<VideoInfo> userWorks = videoInfoMapper.getSelfWorks(userId);
        Set<Integer> pkVideoIdList = userWorks.stream()
                .map(VideoInfo::getPkVideoId)
                .collect(Collectors.toSet());
        return videoInfoService.getUserVideoInfo(pkVideoIdList);
    }
    @ApiOperation("获取用户喜欢的视频接口,用于feign接口调用")
    @GetMapping("/getLikeWorks")
    public List<VideoStreamVO> getLikeWorks(@RequestParam("userId") Integer userId) {
        Set<Integer> pkVideoIdList=(Set)redisUtil.sGet(RedisConstants.LIKE_KEY_PREFIX+userId);
        return videoInfoService.getUserVideoInfo(pkVideoIdList);
    }
    @ApiOperation("获取用户收藏的视频接口,用于feign接口调用")
    @GetMapping("/getCollectWorks")
    public List<VideoStreamVO> getCollectWorks(@RequestParam("pkVideoIdList")Set<Integer>pkVideoIdList) {
        return videoInfoService.getUserVideoInfo(pkVideoIdList);
    }
}
