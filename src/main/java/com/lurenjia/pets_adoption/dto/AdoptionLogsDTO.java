package com.lurenjia.pets_adoption.dto;

import com.lurenjia.pets_adoption.entity.Adoptions;
import com.lurenjia.pets_adoption.entity.Pets;
import com.lurenjia.pets_adoption.entity.Users;
import lombok.Data;

/**
 * @author xuanzhen
 * @date 2024/12/27-21:14
 * @description 领养记录
 */
@Data
public class AdoptionLogsDTO extends Adoptions {


    /**
     * 宠物信息
     */
    private Pets pet;

    /**
     * 领养人信息
     */
    private Users user;
}
