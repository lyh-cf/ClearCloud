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
public class VideoCount implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * VideoId
     */
    @TableId(value = "pk_video_id", type = IdType.AUTO)
    private Integer pkVideoId;

    /**
     * 获赞数
     */
    private Integer likedCount;

    /**
     * 评论数
     */
    private Integer commentedCount;

    /**
     * 收藏数
     */
    private Integer collectedCount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
