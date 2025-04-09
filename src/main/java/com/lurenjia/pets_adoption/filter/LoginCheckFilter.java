package com.lurenjia.pets_adoption.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.AntPathMatcher;

import com.alibaba.fastjson.JSON;
import com.lurenjia.pets_adoption.dto.R;

import lombok.extern.slf4j.Slf4j;

/**
 * 作者： lurenjia
 * 时间： 2023/3/16-12:07
 * 描述： 过滤器，过滤没有登录的请求
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    /**
     * spring提供的用户路径比较的工具类
     */
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 获取请求uri
        String requestURI = request.getRequestURI();
        log.info("获取到请求：{}", requestURI);

        // 不需要处理的请求数据
        String[] urls = new String[] {
                "/web/page/login/**", // 只允许访问登录相关页面
                "/web/js/**", // 允许访问静态资源
                "/web/css/**",
                "/web/images/**",
                "/web/element-ui/**", // 允许访问Element UI资源
                "/users/login",
                "/users/logon",
                "/users/logout",
                "/users/sendMsg",
                "/users/loginByPhone",
                "/doc.html",
                "/webjars/**",
                "/swagger-resources",
                "/v2/api-docs"
        };
        // 进行对比
        boolean flag = check(urls, requestURI);
        // 如果不需要拦截
        if (flag) {
            // 放行
            filterChain.doFilter(request, response);
            return;
        }
        // 判断登录状态
        if (request.getSession().getAttribute("user") != null) {
            // 登录了，放行
            log.info("已经登录，放行！");
            filterChain.doFilter(request, response);
        } else {
            // 未登录，反馈给客户端
            log.info("没有登录的请求，拒绝访问：{}", requestURI);
            response.getWriter().write(JSON.toJSONString(R.error("NoLogin!")));
        }
    }

    /**
     * 对请求路径进行对比，看是否为 不需要 拦截的请求。
     *
     * @param urls
     * @param requestURI
     * @return
     */
    private boolean check(String[] urls, String requestURI) {
        boolean flag;
        for (String url : urls) {
            flag = PATH_MATCHER.match(url, requestURI);
            if (flag) {
                return true;
            }
        }
        return false;
    }
}
