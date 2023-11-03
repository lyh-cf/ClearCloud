package com.clearcloud.videoservice.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clearcloud.base.model.RedisConstants;
import com.clearcloud.videoservice.mapper.VideoInfoMapper;
import com.clearcloud.videoservice.mapstruct.VideoMapstruct;
import com.clearcloud.videoservice.model.pojo.VideoCount;
import com.clearcloud.videoservice.model.pojo.VideoInfo;
import com.clearcloud.videoservice.model.vo.VideoStreamVO;
import com.clearcloud.videoservice.utils.QiNiuUtil;
import com.clearcloud.videoservice.model.vo.UploadVideoVO;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

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
public class VideoInfoService extends ServiceImpl<VideoInfoMapper, VideoInfo>  implements IService<VideoInfo> {
    @Autowired
    private QiNiuUtil qiNiuUtil;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource(name = "redissonClient")
    private RedissonClient redissonClient;
    @Resource
    private VideoInfoMapper videoInfoMapper;
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
    //给游客推视频
    public List<VideoStreamVO>pushVideoForVisitor(){
        return getVideoStreamVOS(getVideoIdSet());
    }
    public List<VideoStreamVO>pushVideoForUser(Integer userId){
        Set<Integer> videoIdSet = getVideoIdSet();
        //过滤看过的视频
        RBloomFilter<Integer> bloomFilter = redissonClient.getBloomFilter(RedisConstants.USER_BLOOM_FILTER_KEY_PREFIX + userId);
        Iterator<Integer> iterator = videoIdSet.iterator();
        while (iterator.hasNext()) {
            if(bloomFilter.contains(iterator.next()))iterator.remove();
        }
        return getVideoStreamVOS(videoIdSet);
    }
    private Set<Integer>getVideoIdSet(){
        //返回与RedisTemplate中定义的值类型(V)相同的值
        Set<Object> objectSet =redisTemplate.opsForZSet().reverseRange(RedisConstants.ALL_PUBLISHED_VIDEO_KEY, 0, RedisConstants.ONCE_PUSH_VIDEO_NUMBER - 1);
        if(objectSet==null) return null;
        Set<Integer> videoIds = new HashSet<>();
        for (Object obj : objectSet) {
            if (obj instanceof Integer) {
                videoIds.add((Integer) obj);
            } else if (obj instanceof String) {
                videoIds.add(Integer.parseInt((String) obj));
            }
        }
        return videoIds;
    }

    private List<VideoStreamVO> getVideoStreamVOS(Set<Integer> videoIds) {
        List<VideoInfo> videosInfo= videoInfoMapper.getRecordsByIds(videoIds);
        List<VideoStreamVO>videoStreamVOList=new ArrayList<>();
        videosInfo.forEach(videoInfo -> {
            VideoCount videoCount=(VideoCount)redisTemplate.opsForValue().get(RedisConstants.VIDEO_COUNT_KEY_PREFIX+videoInfo.getPkVideoId());
            videoStreamVOList.add(VideoMapstruct.INSTANCT.conver(videoInfo,videoCount));
        });
        return videoStreamVOList;
    }
}
