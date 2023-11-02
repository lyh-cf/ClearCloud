package com.clearcloud.videoservice.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/*
 *@title UploadVideoVO
 *@description
 *@author LYH
 *@version 1.0
 *@create 2023/11/2 0:49
 */
@Data
@AllArgsConstructor
public class UploadVideoVO implements Serializable {
    private String videoURL;
    private String coverURL;
}
