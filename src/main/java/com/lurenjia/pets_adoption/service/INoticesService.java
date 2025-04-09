package com.lurenjia.pets_adoption.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lurenjia.pets_adoption.dto.R;
import com.lurenjia.pets_adoption.entity.Notices;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpSession;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lurenjia
 * @since 2023-03-25
 */
public interface INoticesService extends IService<Notices> {

    /**
     * 发布新公告
     * @param notice
     * @param session
     * @return
     */
    R<String> releaseNotice(Notices notice, HttpSession session);

    /**
     * 获取页面信息
     * @param page
     * @param pageSize
     * @param userRealname
     * @param noticeType
     * @return
     */
    R<Page> getList(Integer page, Integer pageSize, String userRealname, Integer noticeType);
}
