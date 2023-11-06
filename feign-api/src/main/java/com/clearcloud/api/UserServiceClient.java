package com.clearcloud.api;

import com.clearcloud.model.AuthorVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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
    void addLikedCount(@RequestParam("userId") Integer userId);
    @GetMapping("/reduceLikedCount")
    void reduceLikedCount(@RequestParam("userId") Integer userId);
    @GetMapping("/collectVideo")
    void collectVideo(@RequestParam("userId") Integer userId,@RequestParam("videoId") Integer videoId);
    @GetMapping("/cancelCollectVideo")
    void cancelCollectVideo(@RequestParam("userId") Integer userId,@RequestParam("videoId") Integer videoId);

    @GetMapping("/getAuthorVO")
    List<AuthorVO> getAuthorVO(@RequestParam("userId") Integer userId, @RequestParam("videoIdList") List<Integer> videoIdList, @RequestParam("authorIdList") List<Integer> authorIdList);
}