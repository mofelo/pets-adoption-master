package com.lurenjia.pets_adoption.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.lurenjia.pets_adoption.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

/**
 * 作者： lurenjia
 * 时间： 2023/3/16-8:56
 * 描述： 配置springmvc
 */
@Slf4j
@Configuration
@EnableSwagger2
@EnableKnife4j
public class WebMvcConfig extends WebMvcConfigurationSupport {
    /**
     * 对静态资源的放行
     * @param registry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("mvc容器已配置：对静态资源的放行!");
        //前端页面
        registry.addResourceHandler("/web/**").addResourceLocations("classpath:/web/");
        //接口文档
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * 扩展mvc框架的消息转换器，使用自定义的。
     * @param converters
     */
    @Override
    protected void extendMessageConverters(final List<HttpMessageConverter<?>> converters) {
        log.info("mvc容器已配置：消息转换器，Long型数值传到前端变为String!");
        //创建消息转换器对象
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        //设置对象转换器
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //设置自定义的转换器为优先使用
        converters.add(0,messageConverter);
    }

    /**
     * 接口文档对象，配置对哪个包进行扫描生成接口文档
     * @return
     */
    @Bean
    public Docket createRestApi(){
        //文档配置
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.lurenjia.pets_adoption.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 文档描述信息
     * @return
     */
    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("宠物管理系统")
                .version("1.0")
                .description("宠物管理系统后端接口文档")
                .build();
    }


}