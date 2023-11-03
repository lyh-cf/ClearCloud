package com.clearcloud.videoservice.controller;

import com.clearcloud.base.model.BaseResponse;
import com.clearcloud.base.model.RedisConstants;
import com.clearcloud.base.utils.JwtUtil;
import com.clearcloud.videoservice.model.vo.VideoStreamVO;
import com.clearcloud.videoservice.service.impl.VideoInfoServiceImpl;
import com.google.common.hash.BloomFilter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/*
 *@title VideoController
 *@description
 *@author LYH
 *@version 1.0
 *@create 2023/11/2 21:50
 */
@RestController
@Api(tags = "视频接口")
public class VideoController {
    @Autowired
    private VideoInfoServiceImpl videoInfoService;
    @Resource(name = "redissonClient")
    private RedissonClient redissonClient;
    @ApiOperation("获取视频流接口")
    @PostMapping("/getVideoStream")
    public BaseResponse<?> getVideoStream(HttpServletRequest httpServletRequest) {
        Integer userId=JwtUtil.getUserId(httpServletRequest);
        //是未登录的用户
        if(userId==null){
            List<VideoStreamVO> videoStreamVOList = videoInfoService.pushVideoForVisitor();
            if(videoStreamVOList==null)return BaseResponse.error("暂无视频获取");
            return BaseResponse.success(videoStreamVOList);
        }
        //已登录的用户
        else{
            List<VideoStreamVO> videoStreamVOList = videoInfoService.pushVideoForUser(userId);
            if(videoStreamVOList==null){
                videoStreamVOList = videoInfoService.pushVideoForVisitor();
                if(videoStreamVOList==null)return BaseResponse.error("暂无视频获取");
                return BaseResponse.success(videoStreamVOList);
            };
            return BaseResponse.success(videoStreamVOList);
        }
    }
    @ApiOperation("增加用户观看记录接口")
    @GetMapping("/addUserWatchRecord")
    public BaseResponse<?> addUserWatchRecord(HttpServletRequest httpServletRequest, @RequestParam Integer videoId) {
        Integer userId=JwtUtil.getUserId(httpServletRequest);
        RBloomFilter<Integer> bloomFilter = redissonClient.getBloomFilter(RedisConstants.USER_BLOOM_FILTER_KEY_PREFIX + userId);
        bloomFilter.add(videoId);
        return BaseResponse.success();
    }
}
