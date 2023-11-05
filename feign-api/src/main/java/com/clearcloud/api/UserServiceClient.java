package com.clearcloud.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/*
 *@title UserService
 *@description
 *@author LYH
 *@version 1.0
 *@create 2023/11/3 20:58
 */
@FeignClient(value = "user-service",path = "/user")
public interface UserServiceClient {
    @GetMapping("/addLikedCount")
    void addLikedCount(Integer userId);
    @GetMapping("/reduceLikedCount")
    void reduceLikedCount(Integer userId);
    @GetMapping("/collectVideo")
    void collectVideo(Integer userId,Integer videoId);
    @GetMapping("/cancelCollectVideo")
    void cancelCollectVideo(Integer userId,Integer videoId);
}