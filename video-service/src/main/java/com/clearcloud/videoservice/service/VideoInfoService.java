package com.clearcloud.videoservice.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clearcloud.api.UserServiceClient;
import com.clearcloud.base.model.RedisConstants;
import com.clearcloud.base.model.SystemConstans;
import com.clearcloud.model.AuthorVO;
import com.clearcloud.model.VideoStreamVO;
import com.clearcloud.videoservice.config.QiNiuConfig;
import com.clearcloud.videoservice.mapper.VideoInfoMapper;
import com.clearcloud.videoservice.mapstruct.VideoMapstruct;
import com.clearcloud.videoservice.model.pojo.VideoCount;
import com.clearcloud.videoservice.model.pojo.VideoInfo;
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
import java.util.stream.Collectors;

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
    @Autowired
    private QiNiuConfig qiNiuConfig;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource(name = "redissonClient")
    private RedissonClient redissonClient;
    @Resource
    private VideoInfoMapper videoInfoMapper;
    @Autowired
    private UserServiceClient userServiceClient;
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
        return new UploadVideoVO(qiNiuConfig.getHostName()+videoKey,qiNiuConfig.getHostName()+pageKey);
    }
    //给游客推视频
    public List<VideoStreamVO>pushVideoForVisitor(String type){
        return getVideoStreamVOS(null,getVideoIdSet(),type);
    }
    public List<VideoStreamVO>pushVideoForUser(Integer userId,String type){
        Set<Integer> videoIdSet = getVideoIdSet();
        //过滤看过的视频
        RBloomFilter<Integer> bloomFilter = redissonClient.getBloomFilter(RedisConstants.USER_BLOOM_FILTER_KEY_PREFIX + userId);
        Iterator<Integer> iterator = videoIdSet.iterator();
        while (iterator.hasNext()) {
            if(bloomFilter.contains(iterator.next()))iterator.remove();
        }
        return getVideoStreamVOS(userId,videoIdSet,type);
    }
    //获取系统发布的所有的视频id
    private Set<Integer>getVideoIdSet(){
        //返回与RedisTemplate中定义的值类型(V)相同的值
        //todo 全部获取
        Set<Object> objectSet =redisTemplate.opsForZSet().reverseRange(RedisConstants.ALL_PUBLISHED_VIDEO_KEY, 0, -1);
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
    private List<VideoStreamVO> getVideoStreamVOS(Integer userId,Set<Integer> videoIds,String type) {
        List<VideoInfo> videosInfo;
        if(SystemConstans.VIDEO_TYPE_RECOMMEND.equals(type)||SystemConstans.VIDEO_TYPE_POPULAR.equals(type)||SystemConstans.VIDEO_TYPE_FOLLOW.equals(type))videosInfo=videoInfoMapper.getRecordsByIds(videoIds);
        else videosInfo=videoInfoMapper.getRecordsByIdsAndType(videoIds,type);
        List<VideoStreamVO>videoStreamVOList=new ArrayList<>();
        List<Integer> pkVideoIdList = videosInfo.stream()
                .map(VideoInfo::getPkVideoId)
                .toList();
        List<Integer> authorIdList = videosInfo.stream()
                .map(VideoInfo::getUserId)
                .toList();
        List<AuthorVO> authorVOList = userServiceClient.getAuthorVO(userId, pkVideoIdList, authorIdList);
        for(int i=0;i<videosInfo.size();i++){
            VideoCount videoCount=(VideoCount)redisTemplate.opsForValue().get(RedisConstants.VIDEO_COUNT_KEY_PREFIX+videosInfo.get(i).getPkVideoId());
            VideoStreamVO videoStreamVO = VideoMapstruct.INSTANCT.conver(videosInfo.get(i), videoCount);
            videoStreamVO.setAuthorVO(authorVOList.get(i));
            videoStreamVOList.add(videoStreamVO);
        }
        return videoStreamVOList;
    }
    public List<VideoStreamVO> getUserVideoInfo(Set<Integer> pkVideoIdList){
        List<VideoStreamVO> videoStreamVOList = new ArrayList<>();
        //批量查作品VideoInfo
        List<VideoInfo> userWorks = videoInfoMapper.getRecordsByIds(pkVideoIdList);
        //拼凑redis的key
        List<String> redisKeysSet = new ArrayList<>();
        for (int i : pkVideoIdList) redisKeysSet.add(RedisConstants.VIDEO_COUNT_KEY_PREFIX + i);
        //批量查作品VideoCount
        List<VideoCount> videoCounts = new ArrayList<>();
        List<Object> objects = redisTemplate.opsForValue().multiGet(redisKeysSet);
        for (Object obj : objects) {
            videoCounts.add((VideoCount) obj);
        }
        //组装对象
        for (int i = 0; i < userWorks.size(); i++) {
            videoStreamVOList.add(VideoMapstruct.INSTANCT.conver(userWorks.get(i), videoCounts.get(i)));
        }
        return videoStreamVOList;
    }
}
