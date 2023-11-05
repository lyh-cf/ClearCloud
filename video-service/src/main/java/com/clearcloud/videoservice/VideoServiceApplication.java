package com.clearcloud.videoservice;

import com.clearcloud.api.UserServiceClient;
import com.clearcloud.config.FeignClientConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@MapperScan("com/clearcloud/videoservice/mapper")
@SpringBootApplication
@EnableFeignClients(defaultConfiguration = FeignClientConfiguration.class,clients = UserServiceClient.class)//自动装配
public class VideoServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(VideoServiceApplication.class, args);
    }

}
