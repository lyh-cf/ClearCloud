package com.clearcloud.userservice.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
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
public class UserInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * UserId
     */
    @TableId(value = "pk_user_id", type = IdType.AUTO)
    private Integer pkUserId;

    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 密码
     */
    private String passWord;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 签名
     */
    private String signature;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
