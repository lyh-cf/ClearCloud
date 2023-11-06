package com.clearcloud.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/*
 *@title VideoStreamVO
 *@description
 *@author LYH
 *@version 1.0
 *@create 2023/11/2 22:06
 */
@Data
public class VideoStreamVO implements Serializable {
    private Integer pkVideoId;//视频id
    private AuthorVO authorVO;//作者信息
    private String type;//视频分区类型
    private String videoDescription;//视频描述/标题
    private String videoCover;//视频封面url
    private String playUrl;//视频播放url
    private Integer likedCount;//点赞数
    private Integer commentedCount;//评论数
    private Integer collectedCount;//收藏数
    private LocalDateTime createTime;//发布时间
}
