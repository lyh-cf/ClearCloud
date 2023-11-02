package com.clearcloud.videoservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.clearcloud.videoservice.mapper.VideoCountMapper;
import com.clearcloud.videoservice.model.pojo.VideoCount;
import com.clearcloud.videoservice.service.IVideoCountService;
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
public class VideoCountServiceImpl extends ServiceImpl<VideoCountMapper, VideoCount> implements IVideoCountService {

}
