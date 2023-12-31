package com.clearcloud.userservice.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.clearcloud.api.VideoServiceClient;
import com.clearcloud.base.model.BaseResponse;
import com.clearcloud.base.model.RedisConstants;
import com.clearcloud.base.utils.JwtUtil;
import com.clearcloud.model.AuthorVO;
import com.clearcloud.model.VideoStreamVO;
import com.clearcloud.userservice.mapper.CollectMapper;
import com.clearcloud.userservice.model.dto.UserSelfInfoDTO;
import com.clearcloud.userservice.mapstruct.UserMapstruct;
import com.clearcloud.userservice.model.pojo.Collect;
import com.clearcloud.userservice.model.pojo.Follow;
import com.clearcloud.userservice.model.pojo.UserCount;
import com.clearcloud.userservice.model.pojo.UserInfo;
import com.clearcloud.userservice.service.CollectService;
import com.clearcloud.userservice.service.UserCountService;
import com.clearcloud.userservice.service.UserInfoService;
import com.clearcloud.userservice.model.vo.UserInformationVO;
import com.clearcloud.userservice.utils.RedisUtil;
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
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/*
 *@title UserController
 *@description
 *@author LYH
 *@version 1.0
 *@create 2023/10/29 19:38
 */
@RestController
@Api(tags = "个人中心接口")
public class UserController {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private CollectService collectService;
    @Autowired
    private VideoServiceClient videoServiceClient;
    @Resource
    private CollectMapper collectMapper;
    @ApiOperation("获取用户个人信息接口")
    @GetMapping("/getUserInformation")
    public BaseResponse<?> getUserInformation(@RequestParam("userId") Integer userId) {
        UserInformationVO userInformation = userInfoService.getUserInformation(userId);
        return BaseResponse.success(userInformation);
    }

    @ApiOperation("获取用户喜欢的视频接口")
    @GetMapping("/getLikeVideos")
    public BaseResponse<?> getLikeVideos(@RequestParam("userId") Integer userId) {
        List<VideoStreamVO> likeWorks = videoServiceClient.getLikeWorks(userId);
        List<Integer> pkVideoIdList = likeWorks.stream()
                .map(VideoStreamVO::getPkVideoId)
                .toList();
        List<Integer> authorIdList = likeWorks.stream()
                .map(VideoStreamVO::getUserId)
                .toList();
        List<AuthorVO> authorVOList = getAuthorVO(userId, pkVideoIdList, authorIdList);
        for (int i = 0; i < likeWorks.size(); i++) {
            likeWorks.get(i).setAuthorVO(authorVOList.get(i));
        }
        return BaseResponse.success(likeWorks);
    }

    @ApiOperation("获取用户收藏的视频接口")
    @GetMapping("/getCollectVideos")
    public BaseResponse<?> getCollectVideos(@RequestParam("userId") Integer userId) {
        List<Integer> collectedVideoId = collectMapper.getCollectedVideoIdByUserId(userId);
        Set<Integer>pkVideoIdList=new HashSet<>(collectedVideoId);
        List<VideoStreamVO> collectWorks = videoServiceClient.getCollectWorks(pkVideoIdList);
        List<Integer> authorIdList = collectWorks.stream()
                .map(VideoStreamVO::getUserId)
                .toList();
        List<AuthorVO> authorVOList = getAuthorVO(userId, collectedVideoId, authorIdList);
        for (int i = 0; i < collectWorks.size(); i++) {
            collectWorks.get(i).setAuthorVO(authorVOList.get(i));
        }
        return BaseResponse.success(collectWorks);
    }

    @ApiOperation("上传头像接口")
    @PostMapping("/uploadAvatar")
    public BaseResponse<?> uploadAvatar(HttpServletRequest httpServletRequest, @RequestParam("avatar") MultipartFile avatar) {
        Integer userId = JwtUtil.getUserId(httpServletRequest);
        String newAvatarURL = userInfoService.uploadAvatar(avatar, userId);
        /*
        旁路缓存，先更新数据库，再删除缓存
        用事务保证操作的原子性
         */
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                try {
                    // 先更新数据库
                    UserInfo userInfo = new UserInfo();
                    userInfo.setPkUserId(userId);
                    userInfo.setAvatar(newAvatarURL);
                    userInfoService.updateById(userInfo);
                    //再删除缓存
                    redisUtil.del(RedisConstants.USER_INFO_KEY_PREFIX + userId);
                } catch (Exception e) {
                    //回滚
                    transactionStatus.setRollbackOnly();
                }
            }
        });
        return BaseResponse.success(newAvatarURL);
    }

    @ApiOperation("修改个人基本信息接口")
    @PutMapping("/updateSelfInfo")
    public BaseResponse<?> updateSelfInfo(@Valid @RequestBody UserSelfInfoDTO userSelfInfoDTO) {
        /*
        旁路缓存，先更新数据库，再删除缓存
        用事务保证操作的原子性
         */
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                try {
                    // 先更新数据库
                    UserInfo userInfo = UserMapstruct.INSTANCT.conver(userSelfInfoDTO);
                    userInfoService.updateById(userInfo);
                    //再删除缓存
                    redisUtil.del(RedisConstants.USER_INFO_KEY_PREFIX + userInfo.getPkUserId());
                } catch (Exception e) {
                    //回滚
                    transactionStatus.setRollbackOnly();
                }
            }
        });
        return BaseResponse.success();
    }

    @ApiOperation("批量获取作者信息接口,用于feign接口调用")
    @GetMapping("/getAuthorVO")
    public List<AuthorVO> getAuthorVO(@RequestParam(value = "userId", required = false) Integer userId, @RequestParam("videoIdList") List<Integer> videoIdList, @RequestParam("authorIdList") List<Integer> authorIdList) {
        return userInfoService.getAuthorVO(userId, videoIdList, authorIdList);
    }

    @ApiOperation("增加获赞接口,用于feign接口调用")
    @GetMapping("/addLikedCount")
    public BaseResponse<?> addLikedCount(@RequestParam("userId") Integer userId) {
        UserCount userCount = (UserCount) redisUtil.get(RedisConstants.USER_COUNT_KEY_PREFIX + userId);
        userCount.setLikedCount(userCount.getLikedCount() + 1);
        redisUtil.set(RedisConstants.USER_COUNT_KEY_PREFIX + userId, userCount);
        return BaseResponse.success();
    }

    @ApiOperation("减少获赞接口,用于feign接口调用")
    @GetMapping("/reduceLikedCount")
    public BaseResponse<?> reduceLikedCount(@RequestParam("userId") Integer userId) {
        UserCount userCount = (UserCount) redisUtil.get(RedisConstants.USER_COUNT_KEY_PREFIX + userId);
        userCount.setLikedCount(userCount.getLikedCount() - 1);
        redisUtil.set(RedisConstants.USER_COUNT_KEY_PREFIX + userId, userCount);
        return BaseResponse.success();
    }

    @ApiOperation("添加收藏接口,用于feign接口调用")
    @GetMapping("/collectVideo")
    public BaseResponse<?> collectVideo(@RequestParam("userId") Integer userId, @RequestParam("videoId") Integer videoId) {
        Collect collect = new Collect();
        collect.setUserId(userId);
        collect.setVideoId(videoId);
        collectService.save(collect);
        return BaseResponse.success();
    }

    @ApiOperation("取消收藏接口,用于feign接口调用")
    @GetMapping("/cancelCollectVideo")
    public BaseResponse<?> cancelCollectVideo(@RequestParam("userId") Integer userId, @RequestParam("videoId") Integer videoId) {
        LambdaUpdateWrapper<Collect> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Collect::getUserId, userId).eq(Collect::getVideoId, videoId);
        collectService.remove(lambdaUpdateWrapper);
        return BaseResponse.success();
    }
}
