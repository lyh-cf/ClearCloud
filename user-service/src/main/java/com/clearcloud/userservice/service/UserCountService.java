package com.clearcloud.userservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clearcloud.userservice.mapper.UserCountMapper;
import com.clearcloud.userservice.model.pojo.UserCount;
import org.springframework.stereotype.Service;
/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lyh
 * @since 2023-10-30
 */


@Service
public class UserCountService extends ServiceImpl<UserCountMapper, UserCount> implements IService<UserCount> {


}
