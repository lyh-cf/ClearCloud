package com.clearcloud.userservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clearcloud.userservice.model.pojo.UserInfo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lyh
 * @since 2023-10-30
 */
public interface UserInfoMapper extends BaseMapper<UserInfo> {
    List<UserInfo>batchQueryUserInfo(List<Integer>userIdList);
}
