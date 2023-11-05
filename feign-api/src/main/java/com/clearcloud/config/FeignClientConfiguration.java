package com.clearcloud.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;

/*
 *文件名: FeignClientConfiguration
 *创建者: 罗义恒
 *描述:
 *创建时间:2023/1/31 18:14
 */
public class FeignClientConfiguration {
    @Bean
    public Logger.Level feignLogLevel(){
        return Logger.Level.BASIC;
    }
}
