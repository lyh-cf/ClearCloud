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
public class Like implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "pk_id", type = IdType.AUTO)
    private Integer pkId;

    /**
     * userId
     */
    private Integer userId;

    /**
     * videoId
     */
    private Integer videoId;

    /**
     * authorId
     */
    private Integer authorId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
