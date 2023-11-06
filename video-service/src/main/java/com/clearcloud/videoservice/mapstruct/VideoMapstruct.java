package com.clearcloud.videoservice.mapstruct;

import com.clearcloud.model.VideoStreamVO;
import com.clearcloud.videoservice.model.pojo.VideoCount;
import com.clearcloud.videoservice.model.pojo.VideoInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/*
 *@title VideoMapstruct
 *@description
 *@author LYH
 *@version 1.0
 *@create 2023/11/3 0:32
 */
@Mapper
public interface VideoMapstruct {
    VideoMapstruct INSTANCT = Mappers.getMapper(VideoMapstruct.class);
    @Mappings({
            @Mapping(source = "videoInfo.pkVideoId", target = "pkVideoId"),
            @Mapping(source = "videoInfo.createTime", target = "createTime")
    })
    VideoStreamVO conver(VideoInfo videoInfo, VideoCount videoCount);
}
