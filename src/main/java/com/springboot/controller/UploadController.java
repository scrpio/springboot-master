package com.springboot.controller;

import com.springboot.common.annotation.SystemLog;
import com.springboot.common.util.FtpUtil;
import com.springboot.common.util.ResultUtil;
import com.springboot.model.vo.Result;
import org.joda.time.DateTime;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@RestController
@RequestMapping("/upload")
public class UploadController {
    private final static String baseUrl = "http://192.168.229.128/images";    // 访问图片时的基础url

    @SystemLog(value = "文件上传")
    @PostMapping("/file")
    public Result<Object> upload(@RequestParam("file") MultipartFile file) throws Exception {
        try {
            //生成uuid作为文件名称
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            //获得文件类型（可以判断如果不是图片，禁止上传）
            String contentType = file.getContentType();
            //获得文件后缀名称
            String imageName = contentType.substring(contentType.indexOf("/") + 1);
            //给上传的图片生成新的文件名
            String imagePath = uuid + "." + imageName;
            String filePath = new DateTime().toString("/yyyy/MM");
            String avatarPath = baseUrl + filePath + "/" + imagePath;
            //获取上传的io流
            InputStream input = file.getInputStream();
            //调用FtpUtil工具类进行上传
            boolean result = FtpUtil.uploadFile(filePath, imagePath, input);
            if (result) {
                return new ResultUtil<Object>().setData(avatarPath);
            } else {
                return new ResultUtil<Object>().setErrorMsg("上传错误");
            }
        } catch (IOException e) {
            return new ResultUtil<Object>().setErrorMsg("上传异常");
        }
    }
}
