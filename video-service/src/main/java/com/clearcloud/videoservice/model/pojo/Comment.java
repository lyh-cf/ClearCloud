package com.clearcloud.videoservice.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author lyh
 * @since 2023-11-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评论Id
     */
    @TableId(value = "pk_comment_id", type = IdType.AUTO)
    private Integer pkCommentId;

    /**
     * 父级评论Id，若无则为-1
     */
    private Integer parentCommentId;

    /**
     * 所属的视频Id
     */
    private Integer videoId;

    /**
     * 评论者UserId
     */
    private Integer userId;

    /**
     * 被评论者UserId
     */
    private Integer repliedUserId;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 评论附带图片URL
     */
    private String commentImage;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
