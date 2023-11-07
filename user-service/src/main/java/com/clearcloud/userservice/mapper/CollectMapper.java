package com.clearcloud.userservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clearcloud.userservice.model.pojo.Collect;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lyh
 * @since 2023-11-05
 */
public interface CollectMapper extends BaseMapper<Collect> {
     List<Integer>getCollectedVideoIdByUserId(@Param("userId")Integer userId);
}
