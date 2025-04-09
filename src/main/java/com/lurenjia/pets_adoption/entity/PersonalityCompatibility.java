package com.lurenjia.pets_adoption.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 性格兼容性实体类
 */
@Data
@TableName("personality_compatibility")
public class PersonalityCompatibility {
    
    @TableId(type = IdType.AUTO)
    private Integer id;
    
    /**
     * 特质1 ID
     */
    private Integer trait1Id;
    
    /**
     * 特质2 ID
     */
    private Integer trait2Id;
    
    /**
     * 兼容性分数（1-10，10表示最兼容）
     */
    private Integer compatibilityScore;
    
    /**
     * 兼容性描述
     */
    private String description;
}
