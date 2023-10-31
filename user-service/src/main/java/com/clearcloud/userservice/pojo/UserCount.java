package com.clearcloud.userservice.pojo;

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
 * @since 2023-10-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserCount implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * UserId
     */
    @TableId(value = "pk_user_id", type = IdType.AUTO)
    private Integer pkUserId;

    /**
     * 关注数
     */
    private Integer followCount;

    /**
     * 粉丝数
     */
    private Integer fanCount;

    /**
     * 获赞数
     */
    private Integer likedCount;

    /**
     * 收藏数
     */
    private Integer collectCount;

    /**
     * 作品数
     */
    private Integer workCount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
