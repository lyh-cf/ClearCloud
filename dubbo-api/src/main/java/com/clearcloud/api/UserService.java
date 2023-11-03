package com.clearcloud.api;

import java.util.List;

/*
 *@title UserService
 *@description
 *@author LYH
 *@version 1.0
 *@create 2023/11/3 20:58
 */
public interface UserService {
    void addLikedCount(Integer userId);
    void reduceLikedCount(Integer userId);
    void collectVideo(Integer userId,Integer videoId);
    void cancelCollectVideo(Integer userId,Integer videoId);
}