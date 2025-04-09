package com.lurenjia.pets_adoption.dto;

import lombok.Data;

/**
 * @author xuanzhen
 * @date 2023/3/26-22:15
 * @description 申请领养的请求数据
 */
@Data
public class AdoptionDTO {
    /**
     * 宠物id
     */
    private  Long petId;

    /**
     * 预约时间
     */
    private String adoptionData;
}
