package com.clearcloud.api;
import com.clearcloud.model.VideoStreamVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

/*
 *@title VideoServiceClient
 *@description
 *@author LYH
 *@version 1.0
 *@create 2023/11/6 0:59
 */
@FeignClient(value = "video-service",path = "/video")
public interface VideoServiceClient {
    @GetMapping("/getUserWorks")
    List<VideoStreamVO> getUserWorks(@RequestParam("userId") Integer userId);
    @GetMapping("/getLikeWorks")
    List<VideoStreamVO> getLikeWorks(@RequestParam("userId") Integer userId);
    @GetMapping("/getCollectWorks")
    List<VideoStreamVO> getCollectWorks(@RequestParam("pkVideoIdList")Set<Integer>pkVideoIdList);
}
