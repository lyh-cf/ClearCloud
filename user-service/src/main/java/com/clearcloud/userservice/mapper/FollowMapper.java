package com.clearcloud.userservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clearcloud.userservice.model.pojo.Follow;
import com.clearcloud.userservice.model.pojo.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lyh
 * @since 2023-11-03
 */
public interface FollowMapper extends BaseMapper<Follow> {
    List<UserInfo>batchQueryFansInfo(@Param("userId")Integer userId, @Param("offset")Integer offset, @Param("rows")Integer rows);
}
