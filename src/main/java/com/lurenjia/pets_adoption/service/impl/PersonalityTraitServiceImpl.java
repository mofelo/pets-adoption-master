package com.lurenjia.pets_adoption.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lurenjia.pets_adoption.entity.PersonalityTrait;
import com.lurenjia.pets_adoption.mapper.PersonalityTraitMapper;
import com.lurenjia.pets_adoption.service.IPersonalityTraitService;
import org.springframework.stereotype.Service;

/**
 * 性格特质Service实现类
 */
@Service
public class PersonalityTraitServiceImpl extends ServiceImpl<PersonalityTraitMapper, PersonalityTrait> implements IPersonalityTraitService {
}
