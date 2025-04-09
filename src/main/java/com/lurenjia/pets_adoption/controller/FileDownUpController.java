package com.lurenjia.pets_adoption.controller;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lurenjia.pets_adoption.dto.R;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 作者： lurenjia
 * 时间： 2023/3/18-20:54
 * 描述： 文件上传下载的控制器
 */
@RestController
@RequestMapping("/common")
@EnableCaching
@Slf4j
public class FileDownUpController {

    @Value("${pets_adoption.file-path}")
    private String imagePath;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 文件下载：
     * 把图片写入响应流中。
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        try {
            // 获取响应流
            ServletOutputStream outputStream = response.getOutputStream();
            // 读取图片到字节数组中
            byte[] image = getImageFileBytes(name);
            // 图片字节数据写入响应流中
            outputStream.write(image);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 通过文件名获取文件的字节数组
     *
     * @param name
     * @return
     * @throws IOException
     */
    private byte[] getImageFileBytes(String name) throws IOException {
        // 尝试从缓存中获取
        try {
            String value = redisTemplate.opsForValue().get("pet:image:" + name);
            if (StrUtil.isNotBlank(value)) {
                log.info("从缓存中获取文件：{}", name);
                return value.getBytes();
            }
        } catch (Exception e) {
            log.warn("从 Redis 获取图片失败，将从文件系统读取: {}", e.getMessage());
        }

        // 从文件系统读取
        File imageFile = new File(imagePath + name);
        if (!imageFile.exists()) {
            log.warn("图片文件不存在: {}, 尝试从资源目录加载", imagePath + name);

            // 尝试从资源目录加载
            File resourceFile = new File("src/main/resources/web/images/pets/" + name);
            if (resourceFile.exists()) {
                byte[] image = FileUtils.readFileToByteArray(resourceFile);
                log.info("从资源目录中获取文件：{}", name);

                // 复制到上传目录
                try {
                    FileUtils.copyFile(resourceFile, imageFile);
                    log.info("复制图片到上传目录: {}", imageFile.getAbsolutePath());
                } catch (Exception e) {
                    log.warn("复制图片失败: {}", e.getMessage());
                }

                return image;
            }

            // 如果资源目录也没有，抛出异常
            log.error("图片文件不存在: {}", imagePath + name);
            throw new IOException("图片文件不存在");
        }

        byte[] image = FileUtils.readFileToByteArray(imageFile);
        log.info("从文件中获取文件：{}", name);

        // 尝试存入缓存
        try {
            redisTemplate.opsForValue().set("pet:image:" + name, Arrays.toString(image), 5L, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.warn("将图片存入 Redis 失败: {}", e.getMessage());
        }

        return image;
    }

    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upLoad(MultipartFile file) {
        // 获取原始文件名
        String fileName = file.getOriginalFilename();
        // 获取到扩展名
        String suffix = fileName.substring(fileName.lastIndexOf("."));

        // 生成唯一文件名
        String realFileName = UUID.randomUUID().toString() + suffix;

        // 获取到配置文件中的文件储存地址，判断文件夹是否存在
        File dir = new File(imagePath);
        if (!dir.exists()) {
            // 文件夹不存在则新建
            dir.mkdir();
            log.info("新建文件夹：{}", imagePath);
        }

        // 文件保存到指定目录下
        try {
            log.info("文件开始保存:{}", realFileName);
            file.transferTo(new File(imagePath + realFileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 把文件名响应和客户端
        return R.success(realFileName);
    }
}
