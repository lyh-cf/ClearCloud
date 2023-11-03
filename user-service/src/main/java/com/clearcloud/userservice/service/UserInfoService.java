package com.clearcloud.userservice.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clearcloud.userservice.mapper.UserInfoMapper;
import com.clearcloud.userservice.model.pojo.UserCount;
import com.clearcloud.userservice.model.pojo.UserInfo;
import com.clearcloud.userservice.utils.QiNiuUtil;
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
 * @since 2023-10-30
 */
@Service
@Slf4j
public class UserInfoService extends ServiceImpl<UserInfoMapper, UserInfo> implements IService<UserInfo> {
    @Autowired
    private QiNiuUtil qiNiuUtil;

    public UserInfo getUserInfo(String userEmail) {
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
