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
public class VideoInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * VideoId
     */
    @TableId(value = "pk_video_id", type = IdType.AUTO)
    private Integer pkVideoId;

    /**
     * 作者的UserId
     */
    private Integer userId;

    /**
     * 分区类型
     */
    private String type;

    /**
     * 视频描述
     */
    private String videoDescription;

    /**
     * 视频封面URL
     */
    private String videoCover;

    /**
     * 播放URL
     */
    private String playUrl;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
