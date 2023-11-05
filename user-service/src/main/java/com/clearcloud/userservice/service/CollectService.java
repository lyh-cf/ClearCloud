package com.clearcloud.userservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clearcloud.userservice.mapper.CollectMapper;
import com.clearcloud.userservice.model.pojo.Collect;
import org.springframework.stereotype.Service;

@Service
public class CollectService extends ServiceImpl<CollectMapper, Collect> implements IService<Collect> {

}
