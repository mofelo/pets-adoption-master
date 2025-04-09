package com.lurenjia.pets_adoption.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lurenjia.pets_adoption.dto.R;
import com.lurenjia.pets_adoption.entity.Notices;
import com.lurenjia.pets_adoption.entity.Users;
import com.lurenjia.pets_adoption.mapper.NoticesMapper;
import com.lurenjia.pets_adoption.service.INoticesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lurenjia
 * @since 2023-03-25
 */
@Service
@Slf4j
public class NoticesServiceImpl extends ServiceImpl<NoticesMapper, Notices> implements INoticesService {
    /**
     * 发布新公告
     *
     * @param notice
     * @param session
     * @return
     */
    @Override
    public R<String> releaseNotice(Notices notice, HttpSession session) {
        //发布人信息
        Users users = (Users) session.getAttribute("user");

        log.info("发布公告者类型为：{}",users.getUserType());
        //发布人为领养人 则公告类型设置为领养日志
        if(users.getUserType()!=1){
            notice.setNoticeType(2);
        }
        //注入属性
        notice.setUserRealname(users.getUserRealname());
        notice.setUserId(users.getUserId());
        notice.setNoticeDate(LocalDate.now());
        //持久化
        this.save(notice);

        return R.success("操作成功！");
    }

    /**
     * 获取页面信息
     *
     * @param page
     * @param pageSize
     * @param userRealname
     * @param noticeType
     * @return
     */
    @Override
    public R<Page> getList(Integer page, Integer pageSize, String userRealname, Integer noticeType) {
        //准备数据对象
        Page<Notices> pageInfo = new Page<>(page, pageSize);

        //查询条件对象
        LambdaQueryWrapper<Notices> queryWrapper = new LambdaQueryWrapper<>();

        //请求携带查询条件：
        queryWrapper.like(userRealname != null && !(userRealname.isEmpty()),
                Notices::getUserRealname, userRealname);
        queryWrapper.eq(noticeType != null,
                Notices::getNoticeType, noticeType);
        queryWrapper.orderByDesc(Notices::getNoticeDate);

        //进行查询
        this.page(pageInfo, queryWrapper);

        //进行响应数据
        return R.success(pageInfo);
    }
}
