package com.lurenjia.pets_adoption.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lurenjia.pets_adoption.dto.R;
import com.lurenjia.pets_adoption.entity.Notices;
import com.lurenjia.pets_adoption.service.INoticesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lurenjia
 * @since 2023-03-25
 */
@RestController
@Slf4j
@RequestMapping("/notices")
public class NoticesController {

    @Autowired
    private INoticesService noticesService;

    /**
     * 发布文章
     * @param notice
     * @return
     */
    @PostMapping()
    public R<String> save(@RequestBody Notices notice, HttpSession session){
        log.info("收到发公告的请求：{}", notice);
        R<String> resp = noticesService.releaseNotice(notice,session);
        return resp;
    }

    /**
     * 分页查询数据
     * @param page
     * @param pageSize
     * @param userRealname 发布者姓名
     * @param noticeType 文章类型
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(Integer page, Integer pageSize, String userRealname, Integer noticeType) {
        log.info("收到获取公告列表的请求,发布者{},文章类型{}", userRealname,noticeType);
        R<Page> resp = noticesService.getList(page,pageSize,userRealname,noticeType);
        return resp;
    }
}

