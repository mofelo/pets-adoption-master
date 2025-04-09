package com.lurenjia.pets_adoption.service.impl;

import java.time.LocalDate;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lurenjia.pets_adoption.dto.AdoptionDTO;
import com.lurenjia.pets_adoption.dto.PetsQueryList;
import com.lurenjia.pets_adoption.dto.R;
import com.lurenjia.pets_adoption.entity.Adoptions;
import com.lurenjia.pets_adoption.entity.Pets;
import com.lurenjia.pets_adoption.entity.Users;
import com.lurenjia.pets_adoption.mapper.PetsMapper;
import com.lurenjia.pets_adoption.service.IAdoptionsService;
import com.lurenjia.pets_adoption.service.IPetsService;
import com.lurenjia.pets_adoption.service.IUsersService;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lurenjia
 * @since 2023-03-25
 */
@Service
@Slf4j
public class PetsServiceImpl extends ServiceImpl<PetsMapper, Pets> implements IPetsService {
    @Autowired
    @Lazy
    private IUsersService usersService;

    @Autowired
    @Lazy
    private IAdoptionsService adoptionsService;

    /**
     * 获取宠物仓库列表信息，从缓存中
     * 缓存key：页码_页码大小_用户类型_宠物昵称_宠物状态（1_8_1_鸭_0）
     *
     * @param page
     * @param pageSize
     * @param pets
     * @param userType
     * @return
     */
    @Cacheable(value = "pet", key = "#page+'_'+#pageSize+'_'+#userType+'_'+#pets.petName+'_'+#pets.petStatus")
    @Override
    public R<Page> getList(Integer page, Integer pageSize, Pets pets, Integer userType) {
        // 查询条件封装
        PetsQueryList petsQueryList = new PetsQueryList(page, pageSize, pets, userType);
        // 从数据库中获取数据
        Page pageInfo = this.getPetsByDB(petsQueryList);
        return R.success(pageInfo);
    }

    /**
     * 从数据库获取指定页码的宠物数据
     * 
     * @param petsQueryList
     * @return
     */
    private Page getPetsByDB(PetsQueryList petsQueryList) {
        // 取出查询条件
        Integer userType = petsQueryList.getUserType();
        Integer page = petsQueryList.getPage();
        Integer pageSize = petsQueryList.getPageSize();
        Pets pets = petsQueryList.getPets();

        // 构建 查询条件对象
        LambdaQueryWrapper<Pets> queryWrapper = new LambdaQueryWrapper<>();
        // 查询条件：宠物昵称、宠物状态，可能为空
        queryWrapper.like(StrUtil.isNotBlank(pets.getPetName()), Pets::getPetName, pets.getPetName());
        queryWrapper.eq(pets.getPetStatus() != null, Pets::getPetStatus, pets.getPetStatus());
        queryWrapper.orderByDesc(Pets::getPetIndata);

        // 如果不是管理员 已经过世的不显示
        if (userType != 1) {
            queryWrapper.ne(Pets::getPetStatus, 3);
        }
        // 准备 页面数据对象
        Page<Pets> pageInfo = new Page<>(page, pageSize);
        // 查询数据 从数据库中
        this.page(pageInfo, queryWrapper);

        // 进行响应数据
        return pageInfo;
    }

    /**
     * 修改宠物信息
     * 
     * @param pet
     * @return
     */
    @Override
    public R<String> updateField(Pets pet) {
        // 验证宠物类型是否为猫或狗
        if (pet.getPetType() != null
                && !com.lurenjia.pets_adoption.utils.PetTypeValidator.isValidPetType(pet.getPetType())) {
            return R.error("宠物类型必须为猫或狗！");
        }

        // 根据id修改宠物信息
        LambdaQueryWrapper<Pets> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Pets::getPetId, pet.getPetId());
        // 修改宠物信息 到数据库
        this.update(pet, queryWrapper);

        log.info("执行了修改操作...............");
        return R.success("修改成功");
    }

    /**
     * 申领宠物操作，操作了用户表、领养信息表、宠物信息表
     *
     * @param adoptionDTO
     * @param session
     * @return
     */
    @Transactional
    @Override
    public R<String> adoptionUp(AdoptionDTO adoptionDTO, HttpSession session) {
        // 获取宠物id
        Long petId = adoptionDTO.getPetId();
        // 获取预约时间
        String time = adoptionDTO.getAdoptionData();
        String[] split = time.split("/");
        LocalDate adoptionData = LocalDate.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]),
                Integer.parseInt(split[2]));

        log.info("收到了一个领养申请：{}，{}", petId, adoptionData);

        // 获取操作者信息 从session中
        Users users = (Users) session.getAttribute("user");
        // 判断用户状态，申领中
        if (users.getUserStatus() == 1) {
            return R.error("一次只能申请一个宠物，请先处理完上一次申请。");
        }
        // 状态异常
        if (users.getUserStatus() == 2) {
            return R.error("用户状态异常，请与管理员进行联系。");
        }

        // 修改用户类型为申领中
        users.setUserStatus(1);
        usersService.updateById(users);

        // 获取宠物信息
        Pets pet = this.getById(petId);
        // 设置宠物类型为被申请领养中
        pet.setPetStatus(2);
        this.updateById(pet);

        // 领养记录表新增一条记录
        Adoptions adoption = new Adoptions();
        adoption.setUserId(users.getUserId());
        adoption.setPetId(petId);
        // 来店时间
        adoption.setAdoDate(adoptionData);
        // 领养状态改变 2处理中
        adoption.setAdoStatus(2);
        adoptionsService.save(adoption);

        return R.success("申请成功！");
    }
}
