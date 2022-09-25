package com.offcn.oss.controller;

import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 客户端直接签名，上传到oss对象存储服务器时，AccessKeyID和AcessKeySecret会暴露在前端页面，因此存在严重的安全隐患
 * 所以我们采用的是搭建oss签名微服务，每次上传文件都需要通过oss签名微服务获取到签名后，然后直接传入到oss中，这里的作用是获取oss服务器签名
 * http://localhost:8888/api/thirdparty/v1/admin/oss/getPolicy?t=1660725209555
 */
@RestController
@RequestMapping("/thirdparty/v1/admin/oss")
public class OssController {

    @Autowired
    OSS ossClient;

    @Value("${spring.cloud.alicloud.access-key}")
    private String accessId;

    @Value("${spring.cloud.alicloud.oss.endpoint}")
    private String endpoint;

    @Value("${spring.cloud.alicloud.oss.bucket}")
    private String bucket;

    @RequestMapping("/getPolicy")
    public Map<String, String> getPolicy() {
        String host = "https://" + bucket + "." + endpoint; // host的格式为 bucketname.endpoint
        String formatDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());//把时间对象设计成指定日期格式字符串
        String dir = formatDate + "/"; // 用户上传文件时指定的前缀。
        Map<String, String> respMap = new LinkedHashMap<String, String>();
        try {
            long expireTime = 30;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);

            //封装accessid、签名等信息返回
            respMap.put("accessid", accessId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));


        } catch (Exception e) {
            // Assert.fail(e.getMessage());
            System.out.println(e.getMessage());
        } finally {
            ossClient.shutdown();
        }
        //{"msg":"invalid token","code":401}
        //http://localhost:8888/api/thirdparty/v1/admin/oss/getPolicy?t=1660728772484
        return respMap;
    }
}

