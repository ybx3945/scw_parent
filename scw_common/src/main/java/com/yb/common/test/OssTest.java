package com.yb.common.test;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;

import java.io.File;

public class OssTest {

    public static void main(String[] args) {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        //oss-cn-beijing.aliyuncs.com
        String endpoint = "http://oss-cn-beijing.aliyuncs.com";
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = "LTAI4GH8W73RvDK3JbMwr5i1";
        String accessKeySecret = "Ba1W9ofraOgHxu4eb1ZrZf76JNTFO8";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 创建PutObjectRequest对象。
        PutObjectRequest putObjectRequest = new PutObjectRequest("20201210-day", "pic/20201210_dog.jpg", new File("E:\\document\\img\\其他\\1544523336-6.jpg"));

        // 上传文件。
        ossClient.putObject(putObjectRequest);

        //  关闭OSSClient。
        ossClient.shutdown();
    }

}
