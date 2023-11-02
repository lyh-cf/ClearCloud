package com.clearcloud.videoservice.utils;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.qiniu.util.StringUtils;
import com.qiniu.util.UrlSafeBase64;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.clearcloud.videoservice.config.QiNiuConfig;
/*
 *@filename: UploadService
 *@author: lyh
 *@date:2023/7/4 17:02
 *@version 1.0
 *@description TODO
 */
@Component
@Slf4j
public class QiNiuUtil {
    @Autowired
    private QiNiuConfig qiNiuConfig;

    private final String pipeline = "default.sys";

    //上传策略中设置persistentOps字段和persistentPipeline字段
    public String getPersistentOps(String videoKey,String pageKey){
        String saveMp4Entry = qiNiuConfig.getBucketName()+":"+videoKey;
        String saveJpgEntry = qiNiuConfig.getBucketName()+":"+pageKey;
        String avthumbMp4Fop ="avthumb/mp4/avsmart/1/vb/1.25m"+"|saveas/"+ UrlSafeBase64.encodeToString(saveMp4Entry);
        String vframeJpgFop = "vframe/jpg/offset/1"+"|saveas/"+  UrlSafeBase64.encodeToString(saveJpgEntry);
        //将多个数据处理指令拼接起来
        return StringUtils.join(new String[]{
                avthumbMp4Fop, vframeJpgFop
        }, ";");
    }
    public String getUpToken(String videoKey,String pageKey,Auth auth) {
        String persistentOpfs=getPersistentOps(videoKey,pageKey);
        log.info("pfops：" + persistentOpfs);
        String token = auth.uploadToken(qiNiuConfig.getBucketName(), null, 3600, new StringMap()
                .putNotEmpty("persistentOps", persistentOpfs)
                .putNotEmpty("persistentPipeline", pipeline), true);
        log.info("token：" + token);
        return token;
    }
    public void upload(byte[] bytes,String videoKey,String pageKey) throws Exception {
        Auth auth = Auth.create(qiNiuConfig.getAccessKey(), qiNiuConfig.getAccessSecret());
        Configuration c = new Configuration(Region.huadongZheJiang2());
        //创建上传对象
        UploadManager uploadManager = new UploadManager(c);
        //调用put方法上传
        Response res = uploadManager.put(bytes, videoKey, getUpToken(videoKey,pageKey,auth));
        //打印返回的信息
        log.info("七牛云接口返回信息："+res.bodyString());
    }
}
