package com.clearcloud.userservice;

import com.clearcloud.api.UserServiceClient;
import com.clearcloud.api.VideoServiceClient;
import com.clearcloud.config.FeignClientConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@MapperScan("com/clearcloud/userservice/mapper")
@SpringBootApplication
@EnableFeignClients(defaultConfiguration = FeignClientConfiguration.class,clients = VideoServiceClient.class)//自动装配
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

}
