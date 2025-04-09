package com.lurenjia.pets_adoption.utils;

import java.util.Collections;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CodeGenerator {
    private static final String URL = "jdbc:mysql://localhost:3306/pets_adoption?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "lurenjia";
    private static final String AUTHOR = "lurenjia";
    private static final String BASE_PACKAGE = "com.lurenjia.pets_adoption";

    public static void main(String[] args) {
        String projectPath = System.getProperty("user.dir");

        FastAutoGenerator.create(URL, USERNAME, PASSWORD)
                // 全局配置
                .globalConfig(builder -> {
                    builder.author(AUTHOR)
                            .enableSwagger()
                            .fileOverride()
                            .outputDir(projectPath + "/src/main/java");
                })
                // 包配置
                .packageConfig(builder -> {
                    builder.parent(BASE_PACKAGE)
                            .pathInfo(Collections.singletonMap(
                                    OutputFile.xml,
                                    projectPath + "/src/main/resources/mapper"));
                })
                // 策略配置
                .strategyConfig(builder -> {
                    builder.entityBuilder()
                            .enableLombok()
                            .enableTableFieldAnnotation();

                    builder.controllerBuilder()
                            .enableRestStyle()
                            .enableHyphenStyle();

                    builder.serviceBuilder()
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImpl");

                    builder.mapperBuilder()
                            .enableMapperAnnotation()
                            .formatMapperFileName("%sMapper")
                            .formatXmlFileName("%sMapper");

                    builder.addTablePrefix("m_");
                })
                // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}