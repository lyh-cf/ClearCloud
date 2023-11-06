package com.clearcloud.videoservice.controller;

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
        List<VideoStreamVO> videoStreamVOList = new ArrayList<>();
        //批量查作品VideoInfo
        List<VideoInfo> userWorks = videoInfoMapper.getSelfWorks(userId);
        List<Integer> pkVideoIdList = userWorks.stream()
                .map(VideoInfo::getPkVideoId)
                .toList();
        //拼凑redis的key
        List<String> redisKeysSet = new ArrayList<>();
        for (int i : pkVideoIdList) redisKeysSet.add(RedisConstants.VIDEO_COUNT_KEY_PREFIX + i);
        //批量查作品VideoCount
        List<VideoCount> videoCounts = new ArrayList<>();
        List<Object> objects = redisTemplate.opsForValue().multiGet(redisKeysSet);
        for (Object obj : objects) {
            videoCounts.add((VideoCount) obj);

        }
        //组装对象
        for (int i = 0; i < userWorks.size(); i++) {
            videoStreamVOList.add(VideoMapstruct.INSTANCT.conver(userWorks.get(i), videoCounts.get(i)));
        }
        return videoStreamVOList;
    }
}
