package com.clearcloud.userservice.service;

import com.clearcloud.base.model.RedisConstants;
import com.clearcloud.userservice.model.dto.RegisterDTO;
import com.clearcloud.userservice.mapstruct.UserMapstruct;
import com.clearcloud.userservice.model.pojo.UserCount;
import com.clearcloud.userservice.model.pojo.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/*
 *@title IAuthServiceImpl
 *@description
 *@author LYH
 *@version 1.0
 *@create 2023/10/31 23:54
 */
@Service
@Slf4j
public class AuthService {
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserCountService userCountMapper;

    @Resource(name = "redissonClient")
    private RedissonClient redissonClient;
    @Transactional
    public void registerUserInformation(RegisterDTO registerDTO) {
        //对象转换
        UserInfo userInfo = UserMapstruct.INSTANCT.conver(registerDTO);
        userInfoService.save(userInfo);//userInfo会保存插入的userId
        UserCount userCount=new UserCount();
        userCount.setPkUserId(userInfo.getPkUserId());
        userCountMapper.save(userCount);
        //给每个用户注册一个bloom过滤器
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter(RedisConstants.USER_BLOOM_FILTER_KEY_PREFIX+userInfo.getPkUserId());
        //初始化，容量1000，错误率千分之三
        bloomFilter.tryInit(RedisConstants.BLOOM_EXPECTED_ELEMENTS, RedisConstants.BLOOM_false_Positive_Rate);
    }
}
