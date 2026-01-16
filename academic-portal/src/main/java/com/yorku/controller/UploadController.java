package com.yorku.controller;

import com.yorku.pojo.Result;
import com.yorku.util.AliyunOSSOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Digital Asset Controller (File Upload)
 * York University Academic Admin Portal
 *
 * Description:
 * Handles the ingestion of binary files (e.g., Faculty headshots, Student ID photos).
 * Integrates with Cloud Object Storage (Aliyun OSS) for scalable asset hosting.
 *
 * @Description: 文件上传控制器 (数字资产管理)
 * 处理教职工/学生证件照的上传请求，对接云存储服务。
 */
@Slf4j
@RestController
public class UploadController {

    @Autowired
    private AliyunOSSOperator aliyunOSSOperator;

    /**
     * Upload Profile Image
     * Receives a multipart file, uploads it to the cloud, and returns the public access URL.
     *
     * 文件上传接口
     * 接收前端传递的文件流，上传至OSS对象存储，并返回可访问的URL地址。
     *
     * @param file The binary file from the frontend / 前端传递的文件
     * @return Result containing the image URL / 包含图片URL的统一返回结果
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws Exception {
        // Log the incoming file details for audit
        // 记录上传请求日志
        log.info("📤 [York U Admin] Initiating Profile Image Upload. Filename: {}", file.getOriginalFilename());

        // Delegate to OSS Operator for storage
        // 调用阿里云OSS工具类进行文件存储
        String url = aliyunOSSOperator.upload(file.getBytes(), file.getOriginalFilename());

        // Log the successful transaction
        // 记录上传成功日志及返回的URL
        log.info("✅ [Upload Success] Asset hosted at: {}", url);

        return Result.success(url);
    }
}