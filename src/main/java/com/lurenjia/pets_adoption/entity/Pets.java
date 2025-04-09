package com.lurenjia.pets_adoption.entity;

import java.io.Serializable;
import java.time.LocalDate;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author lurenjia
 * @since 2023-03-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Pets implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 宠物编号
     */
    @TableId(value = "pet_id", type = IdType.AUTO)
    private Long petId;

    /**
     * 宠物昵称
     */
    private String petName;

    /**
     * 宠物品种
     */
    private String petType;

    /**
     * 宠物性别：1雄，0雌
     */
    private Integer petSex;

    /**
     * 宠物生日，用于获取年龄
     */
    private LocalDate petAge;

    /**
     * 宠物入园时间
     */
    private LocalDate petIndata;

    /**
     * 宠物照片文件名
     */
    private String petImage;

    /**
     * 宠物简介
     */
    private String petIntroduction;

    /**
     * 宠物性格：1外向，0内向
     */
    private Integer petPersonality;

    /**
     * 宠物状态：0待领养，1被领养，2被申领，3离世
     */
    private Integer petStatus;

    /**
     * 领养人id
     */
    private Integer userId;

}
