package com.lurenjia.pets_adoption.controller;

import java.time.LocalDate;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lurenjia.pets_adoption.dto.AdoptionDTO;
import com.lurenjia.pets_adoption.dto.R;
import com.lurenjia.pets_adoption.entity.Pets;
import com.lurenjia.pets_adoption.entity.Users;
import com.lurenjia.pets_adoption.service.IPetsService;

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
@RequestMapping("/pets")
@Slf4j
public class PetsController {
    @Autowired
    private IPetsService petsService;

    /**
     * 分页查询数据
     * 
     * @param page     查询页码
     * @param pageSize 页码大小
     * @param pets     数据，包含了查询条件，可能为空
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(Integer page, Integer pageSize, Pets pets, HttpSession session) {
        log.info("收到获取宠物列表的请求：{}", pets);
        // 获取用户类型
        Integer userType = ((Users) session.getAttribute("user")).getUserType() == 1 ? 1 : 0;
        // 获取数据
        R<Page> resp = petsService.getList(page, pageSize, pets, userType);
        return resp;
    }

    /**
     * 新增一个宠物
     * 
     * @param pet
     * @return
     */
    @CacheEvict(value = "pet", allEntries = true)
    @PostMapping("/save")
    public R<String> save(@RequestBody Pets pet, HttpSession session) {
        log.info("新增宠物信息：{}", pet);

        // 验证宠物类型是否为猫或狗
        if (pet.getPetType() == null
                || !com.lurenjia.pets_adoption.utils.PetTypeValidator.isValidPetType(pet.getPetType())) {
            return R.error("宠物类型必须为猫或狗！");
        }

        // 补充初始数据
        pet.setPetIndata(LocalDate.now());
        pet.setPetStatus(0);

        // 新增宠物信息 到数据库
        petsService.save(pet);
        return R.success("新增成功");
    }

    /**
     * 修改宠物信息
     * 
     * @param pet
     * @return
     */
    @CacheEvict(value = "pet", allEntries = true)
    @PostMapping("/update")
    public R<String> update(@RequestBody Pets pet) {
        log.info("修改宠物信息：{}", pet);
        R<String> resp = petsService.updateField(pet);
        return resp;
    }

    /**
     * 删除一个宠物
     *
     * @param pet
     * @return
     */
    @CacheEvict(value = "pet", allEntries = true)
    @DeleteMapping
    public R<String> remove(@RequestBody Pets pet, HttpSession session) {
        log.info("删除宠物的请求，id为：{}", pet.getPetId());

        LambdaQueryWrapper<Pets> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Pets::getPetId, pet.getPetId());

        petsService.remove(queryWrapper);
        return R.success("删除成功");
    }

    /**
     * 申请领养
     * 
     * @param adoptionDTO 预约时间，格式为：yyyy/MM/dd
     * @param session
     * @return
     */
    @CacheEvict(value = { "adoption", "pet" }, allEntries = true)
    @PostMapping("/adoption")
    public R<String> adoption(@RequestBody AdoptionDTO adoptionDTO, HttpSession session) {
        log.info("被申领宠物id {} ", adoptionDTO.getPetId());
        R<String> resp = petsService.adoptionUp(adoptionDTO, session);
        return resp;
    }
}
