package com.clearcloud.videoservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.clearcloud.videoservice.mapper.LikeMapper;
import com.clearcloud.videoservice.model.pojo.Like;
import com.clearcloud.videoservice.service.ILikeService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lyh
 * @since 2023-11-02
 */
@Service
public class LikeServiceImpl extends ServiceImpl<LikeMapper, Like> implements ILikeService {

}
