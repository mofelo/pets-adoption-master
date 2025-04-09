package com.lurenjia.pets_adoption.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lurenjia.pets_adoption.dto.AdoptionDTO;
import com.lurenjia.pets_adoption.dto.R;
import com.lurenjia.pets_adoption.entity.Pets;
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
public interface IPetsService extends IService<Pets> {

    /**
     * 获取宠物仓库列表信息
     *
     * @param page
     * @param pageSize
     * @param pets
     * @param userType
     * @return
     */
    R<Page> getList(Integer page, Integer pageSize, Pets pets, Integer userType);

    /**
     * 申领宠物操作
     * @param adoptionDTO
     * @param session
     * @return
     */
    R<String> adoptionUp(AdoptionDTO adoptionDTO, HttpSession session);

    /**
     * 修改宠物信息
     * @param pet
     * @return
     */
    R<String> updateField(Pets pet);
}
