package com.clearcloud.videoservice.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clearcloud.videoservice.mapper.VideoInfoMapper;
import com.clearcloud.videoservice.model.pojo.VideoInfo;
import com.clearcloud.videoservice.service.IVideoInfoService;
import com.clearcloud.videoservice.utils.QiNiuUtil;
import com.clearcloud.videoservice.model.vo.UploadVideoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lyh
 * @since 2023-11-02
 */
@Service
@Slf4j
public class VideoInfoServiceImpl extends ServiceImpl<VideoInfoMapper, VideoInfo> implements IVideoInfoService {
    @Autowired
    private QiNiuUtil qiNiuUtil;
    @Override
    public UploadVideoVO uploadVideo(MultipartFile file, Integer userId) {
        //获取原始文件名
        String originalFilename = file.getOriginalFilename();
        //去掉后缀
        int lastIndex = originalFilename.lastIndexOf(".");
        String randomNumbers= String.valueOf(System.currentTimeMillis());
        String videoKey = ""+userId+'-'+randomNumbers+'-' +originalFilename.substring(0, lastIndex)+".mp4";
        String pageKey= ""+userId+'-'+randomNumbers+'-' +originalFilename.substring(0, lastIndex)+".jpg";
        log.info("上传的原文件名：" + originalFilename);
        try {
            qiNiuUtil.upload(file.getBytes(), videoKey,pageKey);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new UploadVideoVO(videoKey,pageKey);
    }
}
