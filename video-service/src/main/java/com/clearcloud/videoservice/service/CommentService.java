package com.clearcloud.videoservice.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clearcloud.videoservice.mapper.CommentMapper;
import com.clearcloud.videoservice.model.pojo.Comment;
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
public class CommentService extends ServiceImpl<CommentMapper, Comment> implements IService<Comment> {

}
