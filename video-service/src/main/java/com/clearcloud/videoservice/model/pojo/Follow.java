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
public class Follow implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Id
     */
    @TableId(value = "pk_id", type = IdType.AUTO)
    private Integer pkId;

    /**
     * UserId
     */
    private Integer userId;

    /**
     * 所关注的用户Id
     */
    private Integer followId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
