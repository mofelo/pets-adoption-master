package com.lurenjia.pets_adoption.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户性格实体类
 */
@Data
@TableName("user_personality")
public class UserPersonality {
    
    @TableId(type = IdType.AUTO)
    private Integer id;
    
    /**
     * 用户ID
     */
    private Integer userId;
    
    /**
     * 特质ID
     */
    private Integer traitId;
    
    /**
     * 特质值（1-10，表示程度）
     */
    private Integer traitValue;
}
