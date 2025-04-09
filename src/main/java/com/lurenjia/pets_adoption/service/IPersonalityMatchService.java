package com.lurenjia.pets_adoption.service;

import com.lurenjia.pets_adoption.dto.R;
import com.lurenjia.pets_adoption.entity.Pets;

import java.util.List;
import java.util.Map;

/**
 * 性格匹配Service接口
 */
public interface IPersonalityMatchService {
    
    /**
     * 根据用户ID获取匹配的宠物列表
     * @param userId 用户ID
     * @return 匹配的宠物列表及匹配分数
     */
    R<List<Map<String, Object>>> getMatchedPets(Integer userId);
    
    /**
     * 计算用户和宠物的匹配分数
     * @param userId 用户ID
     * @param petId 宠物ID
     * @return 匹配分数和匹配详情
     */
    R<Map<String, Object>> calculateMatchScore(Integer userId, Integer petId);
    
    /**
     * 保存用户性格特质
     * @param userId 用户ID
     * @param traits 特质ID和值的映射
     * @return 操作结果
     */
    R<String> saveUserPersonality(Integer userId, Map<Integer, Integer> traits);
    
    /**
     * 保存宠物性格特质
     * @param petId 宠物ID
     * @param traits 特质ID和值的映射
     * @return 操作结果
     */
    R<String> savePetPersonality(Integer petId, Map<Integer, Integer> traits);
}
