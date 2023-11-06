package com.clearcloud.userservice.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clearcloud.api.VideoServiceClient;
import com.clearcloud.base.model.RedisConstants;
import com.clearcloud.model.AuthorVO;
import com.clearcloud.model.VideoStreamVO;
import com.clearcloud.userservice.mapper.CollectMapper;
import com.clearcloud.userservice.mapper.UserInfoMapper;
import com.clearcloud.userservice.mapstruct.UserMapstruct;
import com.clearcloud.userservice.model.pojo.UserCount;
import com.clearcloud.userservice.model.pojo.UserInfo;
import com.clearcloud.userservice.model.vo.UserInformationVO;
import com.clearcloud.userservice.utils.QiNiuUtil;
import com.clearcloud.userservice.utils.RedisUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lyh
 * @since 2023-10-30
 */
@Service
@Slf4j
public class UserInfoService extends ServiceImpl<UserInfoMapper, UserInfo> implements IService<UserInfo> {
    @Autowired
    private QiNiuUtil qiNiuUtil;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserCountService userCountService;
    @Autowired
    private VideoServiceClient videoServiceClient;
    @Resource
    private CollectMapper collectMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    public UserInformationVO getUserInformation(Integer userId) {
        //获取基本信息
        UserInfo userInfo;
        if (redisUtil.hasKey(RedisConstants.USER_INFO_KEY_PREFIX + userId)) {
            userInfo = (UserInfo) redisUtil.get(RedisConstants.USER_INFO_KEY_PREFIX + userId);
        } else {
            userInfo = getById(userId);
            redisUtil.set(RedisConstants.USER_INFO_KEY_PREFIX + userInfo.getPkUserId(), userInfo);
        }
        UserCount userCount;
        if (redisUtil.hasKey(RedisConstants.USER_COUNT_KEY_PREFIX + userId)) {
            userCount = (UserCount) redisUtil.get(RedisConstants.USER_COUNT_KEY_PREFIX + userId);
        } else {
            userCount = userCountService.getById(userId);
            redisUtil.set(RedisConstants.USER_COUNT_KEY_PREFIX + userInfo.getPkUserId(), userCount);
        }
        UserInformationVO userInformationVO = UserMapstruct.INSTANCT.conver(userInfo, userCount);
        //远程调用，请求用户的作品信息
        List<VideoStreamVO> videoStreamVOList = videoServiceClient.getUserWorks(userId);
        List<Integer> pkVideoIdList = videoStreamVOList.stream()
                .map(VideoStreamVO::getPkVideoId)
                .toList();
        List<Integer> authorIdList = new ArrayList<>();
        for(int i=0;i<pkVideoIdList.size();i++) authorIdList.add(userId);//自己的作品
        List<AuthorVO> authorVOList = getAuthorVO(userId, pkVideoIdList, authorIdList);
        for(int i=0;i<videoStreamVOList.size();i++){
            videoStreamVOList.get(i).setAuthorVO(authorVOList.get(i));
        }
        userInformationVO.setVideoInfoList(videoStreamVOList);
        return userInformationVO;
    }
    public List<AuthorVO>getAuthorVO(Integer userId,List<Integer> videoIdList,List<Integer> authorIdList){
        //将作者的UserInfo拼凑成AuthorVO
        List<AuthorVO>authorVOS=new ArrayList<>();
        List<String> redisKeysSet = new ArrayList<>();
        for (int i : authorIdList) redisKeysSet.add(RedisConstants.USER_INFO_KEY_PREFIX + i);
        List<Object> objects = redisTemplate.opsForValue().multiGet(redisKeysSet);
        for (Object obj : objects) {
            authorVOS.add(UserMapstruct.INSTANCT.converToAuthorVO((UserInfo)obj));
        }
        //该用户收藏的所有视频id
        List<Integer> collectedVideoId = collectMapper.getCollectedVideoIdByUserId(userId);
        //该用户关注的所有用户id
        Set<Object> followUserIds=redisTemplate.opsForZSet().range(RedisConstants.FOLLOW_KEY_PREFIX+userId,0,-1);
        Set<Integer> followUserIdsSet=null;
        if (followUserIds != null) {
            followUserIdsSet = followUserIds.stream()
                    .map(obj -> (Integer) obj)
                    .collect(Collectors.toSet());
        }
        //该用户点赞的所有视频id
        Set<Integer> likeVideoIds= (Set<Integer>) redisUtil.get(RedisConstants.LIKE_KEY_PREFIX+userId);
        //组装
        for(int i=0;i<authorVOS.size();i++){
            if(userId!=null){
                if(collectedVideoId==null)authorVOS.get(i).setCollect(false);
                else if (collectedVideoId.contains(videoIdList.get(i)))authorVOS.get(i).setCollect(true);
                if(followUserIdsSet==null)authorVOS.get(i).setFollow(false);
                else if(followUserIdsSet.contains(authorIdList.get(i)))authorVOS.get(i).setFollow(true);
                if(likeVideoIds==null)authorVOS.get(i).setLike(false);
                else if(likeVideoIds.contains(videoIdList.get(i)))authorVOS.get(i).setLike(true);
            }
            else{
                authorVOS.get(i).setCollect(false);
                authorVOS.get(i).setFollow(false);
                authorVOS.get(i).setLike(false);
            }
        }
        return authorVOS;
    }
    public UserInfo getUserInfoByUserEmail(String userEmail) {
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getUserEmail, userEmail);
        return getOne(queryWrapper);
    }

    public boolean checkUserEmailIsExist(String userEmail) {
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        //防止重复注册
        queryWrapper.eq(UserInfo::getUserEmail, userEmail);
        UserInfo result = getOne(queryWrapper);
        return result != null;
    }

    public String uploadAvatar(MultipartFile file,Integer userId) {
        //获取原始文件名
        String originalFilename = file.getOriginalFilename();
        String newFilename = ""+userId+'-'+System.currentTimeMillis()+'-' + originalFilename;
        log.info("上传的原文件名：" + originalFilename);
        //创建新的文件名称
        String fileURL = null;
        try {
            fileURL = qiNiuUtil.uploadByBytes(file.getBytes(), newFilename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileURL;
    }
}
