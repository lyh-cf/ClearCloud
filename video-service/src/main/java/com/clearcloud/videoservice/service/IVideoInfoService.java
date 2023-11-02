package com.clearcloud.videoservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clearcloud.videoservice.model.pojo.VideoInfo;
import com.clearcloud.videoservice.model.vo.UploadVideoVO;
import org.springframework.web.multipart.MultipartFile;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lyh
 * @since 2023-11-02
 */
public interface IVideoInfoService extends IService<VideoInfo> {
     UploadVideoVO uploadVideo(MultipartFile file, Integer userId);
}
