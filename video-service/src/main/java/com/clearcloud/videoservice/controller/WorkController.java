package com.clearcloud.videoservice.controller;

import com.clearcloud.base.model.BaseResponse;
import com.clearcloud.base.utils.JwtUtil;
import com.clearcloud.videoservice.service.impl.VideoInfoServiceImpl;
import com.clearcloud.videoservice.model.vo.UploadVideoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

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
    @ApiOperation("视频上传接口")
    @PutMapping("/uploadVideo")
    public BaseResponse<?> uploadVideo(HttpServletRequest httpServletRequest, @RequestParam("video") MultipartFile video) {
        Integer userId = JwtUtil.getUserId(httpServletRequest);
        UploadVideoVO uploadVideoVO = videoInfoService.uploadVideo(video, userId);
        return BaseResponse.success(uploadVideoVO);
    }


}
