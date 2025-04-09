package com.lurenjia.pets_adoption.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lurenjia.pets_adoption.dto.R;
import com.lurenjia.pets_adoption.entity.Adoptions;
import com.lurenjia.pets_adoption.service.IAdoptionsService;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lurenjia
 * @since 2023-03-25
 */
@RestController
@Slf4j
@RequestMapping("/adoptions")
public class AdoptionsController {
    @Autowired
    private IAdoptionsService adoptionsService;

    /**
     * 获取用户状态为异常的姓名
     * 
     * @return
     */
    @Cacheable(value = "adoption", key = "'error_adopter_name'", unless = "#result == null")
    @GetMapping("/getAnomalousUser")
    public R<List<String>> getUserStatus2() {
        R<List<String>> resp = adoptionsService.getErrUserName();
        return resp;
    }

    /**
     * 获取需要管理员操作的申请数量
     * 
     * @return
     */
    @Cacheable(value = "adoption", key = "'adopter_count'", unless = "#result == null")
    @GetMapping
    public R<Integer> getCount() {
        // 获取 状态为处理中的 申领记录数量
        LambdaQueryWrapper<Adoptions> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Adoptions::getAdoStatus, 2);
        Integer count = Math.toIntExact(adoptionsService.count(queryWrapper));

        log.info("待处理的信息有：{}条", count);
        return R.success(count);
    }

    /**
     * 取消领养宠物
     * 
     * @param adoId
     * @param session
     * @return
     */
    @CacheEvict(value = { "adoption", "pet" }, allEntries = true)
    @PutMapping("/{adoId}")
    public R<String> cancel(@PathVariable Integer adoId, HttpSession session) {
        log.info("取消领养开始处理,领养编号：{}", adoId);
        R<String> resp = adoptionsService.cancelAdoption(adoId.longValue(), session);
        return resp;
    }

    /**
     * 修改领养状态
     * 
     * @param adoId
     * @param flag  1，成功，0失败
     * @return
     */
    @CacheEvict(value = { "adoption", "pet" }, allEntries = true)
    @PutMapping("/{adoId}/{flag}")
    public R<String> update(@PathVariable Integer adoId, @PathVariable Integer flag, HttpSession session) {
        log.info("收到修改领养状态信息请求：{},{}", adoId, flag);
        R<String> resp = adoptionsService.updateAdoptionStatus(adoId.longValue(), flag, session);
        return resp;
    }

    /**
     * 获取领养记录列表
     * 
     * @param page
     * @param pageSize
     * @param petName
     * @param userRealname
     * @param adoptionStatus
     * @param session
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(Integer page, Integer pageSize, String petName, String userRealname, Integer adoptionStatus,
            HttpSession session) {
        log.info("收到获取领养记录列表的请求：页码信息：{}，{}，宠物昵称：{}，领养人：{}", page, pageSize, petName, userRealname);
        R<Page> resp = adoptionsService.getList(page, pageSize, petName, userRealname, adoptionStatus, session);
        return resp;
    }
}
