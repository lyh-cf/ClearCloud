package com.clearcloud.videoservice.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clearcloud.videoservice.mapper.CommentMapper;
import com.clearcloud.videoservice.model.pojo.Comment;
import com.clearcloud.videoservice.service.ICommentService;
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
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

}
