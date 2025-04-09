package com.lurenjia.pets_adoption.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lurenjia.pets_adoption.entity.PetPersonality;
import org.apache.ibatis.annotations.Mapper;

/**
 * 宠物性格Mapper接口
 */
@Mapper
public interface PetPersonalityMapper extends BaseMapper<PetPersonality> {
}
