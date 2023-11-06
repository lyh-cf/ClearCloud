package com.clearcloud.userservice.controller;


import com.clearcloud.base.model.BaseResponse;
import com.clearcloud.base.utils.JwtUtil;
import com.clearcloud.userservice.model.vo.BasicUserInfoVO;
import com.clearcloud.userservice.service.FollowService;
import com.clearcloud.userservice.utils.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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
    private FollowService followService;
    @ApiOperation("关注接口")
    @GetMapping("/follow")
    public BaseResponse<?> follow(@RequestParam("userId") Integer userId, @RequestParam Integer targetUserId) {
        followService.follow(userId,targetUserId);
        return BaseResponse.success();
    }
    @ApiOperation("取关接口")
    @GetMapping("/unfollow")
    public BaseResponse<?> unfollow(@RequestParam("userId") Integer userId, @RequestParam Integer targetUserId) {
        followService.unFollow(userId,targetUserId);
        return BaseResponse.success();
    }
    @ApiOperation("分页获取关注列表接口")
    @GetMapping("/getFollowList")
    public BaseResponse<?> getFollowList(@RequestParam("userId") Integer userId, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer pageSize) {
        List<BasicUserInfoVO> followListByPage = followService.getFollowListByPage(userId, page, pageSize);
        return BaseResponse.success(followListByPage);
    }
    @ApiOperation("获取粉丝列表接口")
    @GetMapping("/getFansList")
    public BaseResponse<?> getFansList(@RequestParam("userId") Integer userId,@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer pageSize) {
        List<BasicUserInfoVO>basicUserInfoVOList=followService.getFanListByPage(userId,page,pageSize);
        return BaseResponse.success(basicUserInfoVOList);
    }
}

