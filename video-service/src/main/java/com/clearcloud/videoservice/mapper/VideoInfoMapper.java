package com.clearcloud.videoservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clearcloud.videoservice.model.pojo.VideoInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lyh
 * @since 2023-11-02
 */
public interface VideoInfoMapper extends BaseMapper<VideoInfo> {
    List<VideoInfo> getRecordsByIds(@Param("videoIds") Set<Integer> videoIds);
    List<VideoInfo> getRecordsByIdsAndType(@Param("videoIds") Set<Integer> videoIds,@Param("type")String type);

    List<VideoInfo>getSelfWorks(@Param("userId")Integer userId);
}
