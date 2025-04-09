package com.lurenjia.pets_adoption;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
// 声明式注解：使用注解注册servlet、filter、listener
@ServletComponentScan
// 声明式注解：使用注解注册事务管理器
@EnableTransactionManagement
// 声明式注解：使用注解注册缓存操作
@EnableCaching
// 启用springmvc
@EnableWebMvc
public class PetsAdoptionApplication {

    @Value("${pets_adoption.file-path}")
    private String uploadPath;

    public static void main(String[] args) {
        SpringApplication.run(PetsAdoptionApplication.class, args);
    }

    @Bean
    public CommandLineRunner init() {
        return args -> {
            // 确保上传目录存在
            Path path = Paths.get(uploadPath);
            if (!Files.exists(path)) {
                try {
                    Files.createDirectories(path);
                    System.out.println("创建上传目录: " + path.toAbsolutePath());
                } catch (Exception e) {
                    System.err.println("无法创建上传目录: " + e.getMessage());
                }
            } else {
                System.out.println("上传目录已存在: " + path.toAbsolutePath());
            }

            // 复制资源目录中的图片到上传目录
            copyResourceImages();
        };
    }

    private void copyResourceImages() {
        try {
            // 获取资源目录中的图片
            File resourceDir = new File("src/main/resources/web/images/pets");
            if (resourceDir.exists() && resourceDir.isDirectory()) {
                File[] imageFiles = resourceDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg") ||
                        name.toLowerCase().endsWith(".png") ||
                        name.toLowerCase().endsWith(".jpeg"));

                if (imageFiles != null) {
                    for (File imageFile : imageFiles) {
                        // 复制到上传目录
                        File destFile = new File(uploadPath + imageFile.getName());
                        if (!destFile.exists()) {
                            Files.copy(imageFile.toPath(), destFile.toPath());
                            System.out.println("复制图片: " + imageFile.getName() + " 到 " + destFile.getAbsolutePath());
                        }
                    }
                }
            } else {
                System.err.println("宠物图片目录不存在: " + resourceDir.getAbsolutePath());
            }
        } catch (Exception e) {
            System.err.println("复制资源图片失败: " + e.getMessage());
        }
    }
}
