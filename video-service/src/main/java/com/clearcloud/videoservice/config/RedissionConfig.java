package com.clearcloud.videoservice.config;

/*
 *@title RedissionConfig
 *@description
 *@author LYH
 *@version 1.0
 *@create 2023/11/3 15:43
 */

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redission配置类
 */
@Slf4j
@Configuration
public class RedissionConfig {

    @Autowired
    private RedisProperties redisProperties;

    @Bean
    public RedissonClient redissonClient() {
        RedissonClient redissonClient;

        Config config = new Config();
        //starter依赖进来的redisson要以redis://开头，其他不用
        String url = "redis://"+ redisProperties.getHost() + ":" + redisProperties.getPort();
        config.useSingleServer().setAddress(url)
                .setPassword(redisProperties.getPassword())
                .setDatabase(redisProperties.getDatabase());

        try {
            redissonClient = Redisson.create(config);
            return redissonClient;
        } catch (Exception e) {
            log.error("RedissonClient init redis url:[{}], Exception:", url, e);
            return null;
        }
    }

}