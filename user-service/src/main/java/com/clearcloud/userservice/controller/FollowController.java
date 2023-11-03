package com.clearcloud.userservice.controller;


import com.clearcloud.base.model.BaseResponse;
import com.clearcloud.base.model.RedisConstants;
import com.clearcloud.base.utils.JwtUtil;
import com.clearcloud.userservice.mapstruct.UserMapstruct;
import com.clearcloud.userservice.model.pojo.UserInfo;
import com.clearcloud.userservice.model.vo.BasicUserInfoVO;
import com.clearcloud.userservice.service.impl.FollowServiceImpl;
import com.clearcloud.userservice.utils.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lyh
 * @since 2023-11-02
 */
@RestController
@Api(tags = "用户之间交互接口")
public class FollowController {
    @Autowired
    private FollowServiceImpl followService;
    @Autowired
    private RedisUtil redisUtil;

    @ApiOperation("关注接口")
    @GetMapping("/follow")
    public BaseResponse<?> follow(HttpServletRequest httpServletRequest, @RequestParam Integer targetUserId) {
        Integer userId = JwtUtil.getUserId(httpServletRequest);
        followService.follow(userId,targetUserId);
        return BaseResponse.success();
    }
    @ApiOperation("取关接口")
    @GetMapping("/follow")
    public BaseResponse<?> unFollow(HttpServletRequest httpServletRequest, @RequestParam Integer targetUserId) {
        Integer userId = JwtUtil.getUserId(httpServletRequest);
        followService.unFollow(userId,targetUserId);
        return BaseResponse.success();
    }
    @ApiOperation("获取关注列表接口")
    @GetMapping("/getFollowList")
    public BaseResponse<?> getFollowList(HttpServletRequest httpServletRequest) {
        Integer userId = JwtUtil.getUserId(httpServletRequest);
        List<BasicUserInfoVO>basicUserInfoVOList=new ArrayList<>();
        Set<Integer> set=(Set<Integer>) redisUtil.get(RedisConstants.FOLLOW_KEY_PREFIX + userId);
        set.forEach(followId->{
            //todo
            UserInfo userInfo=(UserInfo) redisUtil.get(RedisConstants.USER_INFO_KEY_PREFIX+followId);
            basicUserInfoVOList.add(UserMapstruct.INSTANCT.converToBasicUserInfoVO(userInfo));
        });
        return BaseResponse.success(basicUserInfoVOList);
    }
    @ApiOperation("获取粉丝列表接口")
    @GetMapping("/getFansList")
    public BaseResponse<?> getFansList(HttpServletRequest httpServletRequest) {
        Integer userId = JwtUtil.getUserId(httpServletRequest);
        List<BasicUserInfoVO>basicUserInfoVOList=new ArrayList<>();
        Set<Integer> set=(Set<Integer>) redisUtil.get(RedisConstants.FAN_KEY_PREFIX + userId);
        set.forEach(fanId->{
            //todo
            UserInfo userInfo=(UserInfo) redisUtil.get(RedisConstants.USER_INFO_KEY_PREFIX+fanId);
            basicUserInfoVOList.add(UserMapstruct.INSTANCT.converToBasicUserInfoVO(userInfo));
        });
        return BaseResponse.success(basicUserInfoVOList);
    }
}

