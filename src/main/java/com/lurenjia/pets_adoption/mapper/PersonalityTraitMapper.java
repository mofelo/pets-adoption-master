package com.lurenjia.pets_adoption.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lurenjia.pets_adoption.entity.PersonalityTrait;
import org.apache.ibatis.annotations.Mapper;

/**
 * 性格特质Mapper接口
 */
@Mapper
public interface PersonalityTraitMapper extends BaseMapper<PersonalityTrait> {
}
