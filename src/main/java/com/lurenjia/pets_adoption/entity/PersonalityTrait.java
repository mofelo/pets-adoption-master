package com.lurenjia.pets_adoption.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 性格特质实体类
 */
@Data
@TableName("personality_traits")
public class PersonalityTrait {
    
    @TableId(type = IdType.AUTO)
    private Integer traitId;
    
    /**
     * 特质名称
     */
    private String traitName;
    
    /**
     * 特质描述
     */
    private String traitDescription;
    
    /**
     * 特质类型：1-性格特质，2-情绪特质
     */
    private Integer traitType;
}
