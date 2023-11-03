package com.clearcloud.base.model;

/*
 *@title RedisConstants
 *@description
 *@author LYH
 *@version 1.0
 *@create 2023/10/31 23:39
 */
public class RedisConstants {
    //用户信息
    public static final String USER_INFO_KEY_PREFIX="ClearCloud:UserInfo:";
    public static final String USER_COUNT_KEY_PREFIX="ClearCloud:UserCount:";
    public static final int USER_INFORMATION_TTL=60*60*24;//单位s
    public static final String FOLLOW_KEY_PREFIX="ClearCloud:Follow:";
    public static final String FAN_KEY_PREFIX="ClearCloud:Fan:";
    public static final String LIKE_KEY_PREFIX="ClearCloud:Like:";
//    public static final int LIKE_KEY_TTL=60*60*24;
    public static final String COLLECT_KEY_PREFIX="ClearCloud:Collect:";
//    public static final int COLLECT_KEY_TTL=60*60*24;
    //视频信息
    public static final String ALL_PUBLISHED_VIDEO_KEY="ClearCloud:PublishedVideo";
    public static final String VIDEO_COUNT_KEY_PREFIX="ClearCloud:VideoCount:";
    //布隆过滤
    public static final String USER_BLOOM_FILTER_KEY_PREFIX="ClearCloud:UserBloom:";
    //预计的数据量
    public static final int BLOOM_EXPECTED_ELEMENTS=1000;
    //误报率
    public static final double BLOOM_false_Positive_Rate=0.001;
    //一次推的视频数
    public static final int ONCE_PUSH_VIDEO_NUMBER=20;

}
