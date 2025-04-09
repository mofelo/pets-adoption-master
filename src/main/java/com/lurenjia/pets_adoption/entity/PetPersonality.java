package com.lurenjia.pets_adoption.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 宠物性格实体类
 */
@Data
@TableName("pet_personality")
public class PetPersonality {
    
    @TableId(type = IdType.AUTO)
    private Integer id;
    
    /**
     * 宠物ID
     */
    private Integer petId;
    
    /**
     * 特质ID
     */
    private Integer traitId;
    
    /**
     * 特质值（1-10，表示程度）
     */
    private Integer traitValue;
}
