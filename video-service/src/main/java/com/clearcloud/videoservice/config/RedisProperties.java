package com.clearcloud.videoservice.config;

/*
 *@title RedisProperties
 *@description
 *@author LYH
 *@version 1.0
 *@create 2023/11/3 15:40
 */

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Redis配置文件的参数
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.redis")
public class RedisProperties {
    private String host;
    private int port;
    private String password;
    private int database;

}
