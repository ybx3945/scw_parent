package com.yb.common.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OSSTemplate {

    private String endpoint; // "http://oss-cn-hangzhou.aliyuncs.com";
    private String accessKeyId; // "<yourAccessKeyId>";
    private String accessKeySecret; // "<yourAccessKeySecret>";
    private String bucketName;
    private String bucketDomain;

    /**
     * 上传文件
     * @param inputStream 文件流
     * @param fileName 文件名
     * @return 文件的URL路径
     */
    public String upload(InputStream inputStream, String fileName) {
        // 由于未来项目中文件会比较多，管理不方便，因此可以每天创建一个目录用于管理当天的文件
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dirName = dateFormat.format(new Date());
        // 为避免文件名重复，需在文件名前加后缀UUID
        fileName = UUID.randomUUID().toString().replace("-", "") + "_" + fileName;
        // OSS的客户端
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 上传
        ossClient.putObject(bucketName, "pic/" + dirName + "/" + fileName, inputStream);
        // 上传之后文件的路径
        String url = bucketDomain + "/pic/" + dirName + "/" + fileName;
        ossClient.shutdown();
        System.out.println("文件存储的路径是：" + url);
        return url;
    }
}
