package com.clearcloud.userservice.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clearcloud.base.model.RedisConstants;
import com.clearcloud.userservice.mapper.FollowMapper;
import com.clearcloud.userservice.mapper.UserInfoMapper;
import com.clearcloud.userservice.mapstruct.UserMapstruct;
import com.clearcloud.userservice.model.pojo.Follow;
import com.clearcloud.userservice.model.pojo.UserCount;
import com.clearcloud.userservice.model.pojo.UserInfo;
import com.clearcloud.userservice.model.vo.BasicUserInfoVO;
import com.clearcloud.userservice.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lyh
 * @since 2023-11-03
 */
@Service
public class FollowService extends ServiceImpl<FollowMapper, Follow> implements IService<Follow> {
    @Autowired
    private RedisUtil redisUtil;
//    @Resource
//    private RedisTemplate<String, Integer> redisIntegerTemplate;//不能这样注入
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private FollowMapper followMapper;

    @Transactional
    public void follow(Integer userId, Integer targetUserId) {
        //先写数据库
        Follow follow = new Follow();
        follow.setUserId(userId);
        follow.setFollowId(targetUserId);
        save(follow);
        //再写缓存
        redisUtil.sSet(RedisConstants.FOLLOW_KEY_PREFIX + userId, targetUserId);
        //修改用户统计信息
        UserCount targetUserCount = (UserCount) redisUtil.get(RedisConstants.USER_COUNT_KEY_PREFIX + targetUserId);
        targetUserCount.setFanCount(targetUserCount.getFanCount() + 1);
        redisUtil.set(RedisConstants.USER_COUNT_KEY_PREFIX + targetUserId, targetUserCount);
        UserCount userCount = (UserCount) redisUtil.get(RedisConstants.USER_COUNT_KEY_PREFIX + userId);
        userCount.setFollowCount(userCount.getFollowCount() + 1);
        redisUtil.set(RedisConstants.USER_COUNT_KEY_PREFIX + userId, userCount);
    }

    @Transactional
    public void unFollow(Integer userId, Integer targetUserId) {
        //先写数据库
        LambdaUpdateWrapper<Follow> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Follow::getUserId, userId).eq(Follow::getFollowId, targetUserId);
        remove(lambdaUpdateWrapper);
        //再写缓存
        redisUtil.setRemove(RedisConstants.FOLLOW_KEY_PREFIX + userId, targetUserId);
        //修改用户统计信息
        UserCount targetUserCount = (UserCount) redisUtil.get(RedisConstants.USER_COUNT_KEY_PREFIX + targetUserId);
        targetUserCount.setFanCount(targetUserCount.getFanCount() - 1);
        redisUtil.set(RedisConstants.USER_COUNT_KEY_PREFIX + targetUserId, targetUserCount);
        UserCount userCount = (UserCount) redisUtil.get(RedisConstants.USER_COUNT_KEY_PREFIX + userId);
        userCount.setFollowCount(userCount.getFollowCount() - 1);
        redisUtil.set(RedisConstants.USER_COUNT_KEY_PREFIX + userId, userCount);
    }
    private Set<Integer>getFollowUserIdSet(Integer userId, Integer start, Integer end){
        //返回与RedisTemplate中定义的值类型(V)相同的值
        Set<Object> objectSet =redisTemplate.opsForZSet().reverseRange(RedisConstants.FOLLOW_KEY_PREFIX + userId, start, end);
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
    @Transactional
    public List<BasicUserInfoVO> getFollowListByPage(Integer userId, Integer page, Integer pageSize) {
        List<BasicUserInfoVO> basicUserInfoVOList = new ArrayList<>();
        //1.从缓存查询目标用户的ids
        int start = (page - 1) * pageSize;
        int end = page * pageSize - 1;
        Set<Integer> userIds = getFollowUserIdSet(userId,start,end);
        //2.转换成redis里的key
        Set<String> redisKeysSet = new HashSet<>();
        if (userIds != null) {
            for (int i : userIds) redisKeysSet.add(RedisConstants.USER_INFO_KEY_PREFIX + i);
        }
        //3.批量从缓存中获取UserInfo对象
        //批量get数据
        Map<String, Object> cachedUserInfoList = redisUtil.batchGet(redisKeysSet);
        //4.组装没有命中的userId
        List<Integer> noHitIdList = new ArrayList<>();
        if (userIds != null) {
            for (Integer i : userIds) {
                if (!cachedUserInfoList.containsKey(i)) noHitIdList.add(i);
            }
        }
        //5.将没有命中缓存的UserInfo从数据库中批量查出来，并批量加载到缓存中
        List<UserInfo> noHitUserInfoList = userInfoMapper.batchQueryUserInfo(noHitIdList);
        Map<String, UserInfo> noHitUserInfoMap =
                noHitUserInfoList.stream()
                        .collect(
                                Collectors.toMap(userInfo -> RedisConstants.USER_INFO_KEY_PREFIX+userInfo.getPkUserId(), Function.identity())
                        );
        redisUtil.batchSet(noHitUserInfoMap);
        //6.组装对象列表
        cachedUserInfoList.putAll(noHitUserInfoMap);
        cachedUserInfoList.forEach((key, value) -> {
            if (value instanceof UserInfo) {
                basicUserInfoVOList.add(UserMapstruct.INSTANCT.converToBasicUserInfoVO((UserInfo) value));
            }
        });
        return basicUserInfoVOList;
    }
    public List<BasicUserInfoVO> getFanListByPage(Integer userId, Integer page, Integer pageSize) {
        List<BasicUserInfoVO> basicUserInfoVOList = new ArrayList<>();
        List<UserInfo> userInfos = followMapper.batchQueryFansInfo(userId, page, pageSize);
        for(UserInfo userInfo:userInfos){
            basicUserInfoVOList.add(UserMapstruct.INSTANCT.converToBasicUserInfoVO(userInfo));
        }
        return basicUserInfoVOList;
    }
}
