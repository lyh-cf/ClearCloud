package com.clearcloud.userservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clearcloud.userservice.mapper.UserCountMapper;
import com.clearcloud.userservice.pojo.UserCount;
import com.clearcloud.userservice.service.IUserCountService;
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
public class UserCountServiceImpl extends ServiceImpl<UserCountMapper, UserCount> implements IUserCountService {

}
