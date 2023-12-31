package com.clearcloud.userservice.model.vo;

import com.clearcloud.model.VideoStreamVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/*
 *@title UserInformationVO
 *@description
 *@author LYH
 *@version 1.0
 *@create 2023/11/1 17:15
 */
@Data
public class UserInformationVO implements Serializable {
    private Integer pkUserId;
    private List<VideoStreamVO> videoInfoList;
    private String userEmail;
    private String nickName;
    private String avatar;
    private String signature;
    private Integer followCount;
    private Integer fanCount;
    private Integer likedCount;
}
