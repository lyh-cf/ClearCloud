package com.clearcloud.userservice.controller;

import com.clearcloud.base.model.BaseResponse;
import com.clearcloud.base.model.RedisConstants;
import com.clearcloud.base.utils.JwtUtil;
import com.clearcloud.userservice.model.dto.UserSelfInfoDTO;
import com.clearcloud.userservice.mapstruct.UserMapstruct;
import com.clearcloud.userservice.model.pojo.UserCount;
import com.clearcloud.userservice.model.pojo.UserInfo;
import com.clearcloud.userservice.service.UserCountService;
import com.clearcloud.userservice.service.UserInfoService;
import com.clearcloud.userservice.utils.RedisUtil;
import com.clearcloud.userservice.model.vo.UserInformationVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/*
 *@title UserController
 *@description
 *@author LYH
 *@version 1.0
 *@create 2023/10/29 19:38
 */
@RestController
@Api(tags = "个人中心接口")
public class UserController {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserCountService userCountService;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @ApiOperation("获取用户个人信息接口")
    @GetMapping("/getUserInformation")
    public BaseResponse<?> getUserInformation(HttpServletRequest httpServletRequest) {
        Integer userId = JwtUtil.getUserId(httpServletRequest);
        UserInfo userInfo;
        if (redisUtil.hasKey(RedisConstants.USER_INFO_KEY_PREFIX + userId)) {
            userInfo = (UserInfo) redisUtil.get(RedisConstants.USER_INFO_KEY_PREFIX + userId);
        } else {
            userInfo = userInfoService.getById(userId);
            redisUtil.set(RedisConstants.USER_INFO_KEY_PREFIX + userInfo.getPkUserId(), userInfo, RedisConstants.USER_INFORMATION_TTL);
        }
        UserCount userCount;
        if (redisUtil.hasKey(RedisConstants.USER_COUNT_KEY_PREFIX + userId)) {
            userCount = (UserCount) redisUtil.get(RedisConstants.USER_COUNT_KEY_PREFIX + userId);
        } else {
            userCount = userCountService.getById(userId);
            redisUtil.set(RedisConstants.USER_COUNT_KEY_PREFIX + userInfo.getPkUserId(), userCount, RedisConstants.USER_INFORMATION_TTL);
        }
        UserInformationVO userInformationVO = UserMapstruct.INSTANCT.conver(userInfo, userCount);
        return BaseResponse.success(userInformationVO);
    }

    @ApiOperation("上传头像接口")
    @PostMapping("/uploadAvatar")
    public BaseResponse<?> uploadAvatar(HttpServletRequest httpServletRequest, @RequestParam("avatar") MultipartFile avatar) {
        Integer userId = JwtUtil.getUserId(httpServletRequest);
        String newAvatarURL=userInfoService.uploadAvatar(avatar,userId);
        /*
        旁路缓存，先更新数据库，再删除缓存
        用事务保证操作的原子性
         */
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                try {
                    // 先更新数据库
                    UserInfo userInfo=new UserInfo();
                    userInfo.setPkUserId(userId);
                    userInfo.setAvatar(newAvatarURL);
                    userInfoService.updateById(userInfo);
                    //再删除缓存
                    redisUtil.del(RedisConstants.USER_INFO_KEY_PREFIX + userId);
                } catch (Exception e){
                    //回滚
                    transactionStatus.setRollbackOnly();
                }
            }
        });
        return BaseResponse.success(newAvatarURL);
    }
    @ApiOperation("修改个人基本信息接口")
    @PutMapping("/updateSelfInfo")
    public BaseResponse<?> updateSelfInfo(@Valid @RequestBody UserSelfInfoDTO userSelfInfoDTO) {
        /*
        旁路缓存，先更新数据库，再删除缓存
        用事务保证操作的原子性
         */
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                try {
                    // 先更新数据库
                    UserInfo userInfo=UserMapstruct.INSTANCT.conver(userSelfInfoDTO);
                    userInfoService.updateById(userInfo);
                    //再删除缓存
                    redisUtil.del(RedisConstants.USER_INFO_KEY_PREFIX + userInfo.getPkUserId());
                } catch (Exception e){
                    //回滚
                    transactionStatus.setRollbackOnly();
                }
            }
        });
        return BaseResponse.success();
    }

}
