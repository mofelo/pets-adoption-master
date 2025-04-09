package com.lurenjia.pets_adoption.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lurenjia.pets_adoption.dto.R;
import com.lurenjia.pets_adoption.entity.PersonalityCompatibility;
import com.lurenjia.pets_adoption.entity.PersonalityTrait;
import com.lurenjia.pets_adoption.entity.PetPersonality;
import com.lurenjia.pets_adoption.entity.Pets;
import com.lurenjia.pets_adoption.entity.UserPersonality;
import com.lurenjia.pets_adoption.mapper.PersonalityCompatibilityMapper;
import com.lurenjia.pets_adoption.mapper.PersonalityTraitMapper;
import com.lurenjia.pets_adoption.mapper.PetPersonalityMapper;
import com.lurenjia.pets_adoption.mapper.PetsMapper;
import com.lurenjia.pets_adoption.mapper.UserPersonalityMapper;
import com.lurenjia.pets_adoption.service.IPersonalityMatchService;

import lombok.extern.slf4j.Slf4j;

/**
 * 性格匹配Service实现类
 */
@Service
@Slf4j
public class PersonalityMatchServiceImpl implements IPersonalityMatchService {

    @Autowired
    private UserPersonalityMapper userPersonalityMapper;

    @Autowired
    private PetPersonalityMapper petPersonalityMapper;

    @Autowired
    private PersonalityCompatibilityMapper compatibilityMapper;

    @Autowired
    private PersonalityTraitMapper traitMapper;

    @Autowired
    private PetsMapper petsMapper;

    /**
     * 根据用户ID获取匹配的宠物列表
     * 
     * @param userId 用户ID
     * @return 匹配的宠物列表及匹配分数
     */
    @Override
    public R<List<Map<String, Object>>> getMatchedPets(Integer userId) {
        try {
            // 获取所有待领养的宠物
            LambdaQueryWrapper<Pets> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Pets::getPetStatus, 0); // 待领养状态
            List<Pets> availablePets = petsMapper.selectList(queryWrapper);

            if (availablePets.isEmpty()) {
                return R.success(new ArrayList<>());
            }

            // 计算每个宠物与用户的匹配分数
            List<Map<String, Object>> matchedPets = new ArrayList<>();
            for (Pets pet : availablePets) {
                R<Map<String, Object>> matchResult = calculateMatchScore(userId, pet.getPetId().intValue());
                if (matchResult.getCode() == 1) {
                    Map<String, Object> petInfo = new HashMap<>();
                    petInfo.put("pet", pet);
                    petInfo.put("matchScore", matchResult.getData().get("score"));
                    petInfo.put("matchDetails", matchResult.getData().get("details"));
                    matchedPets.add(petInfo);
                }
            }

            // 按匹配分数降序排序
            matchedPets.sort((a, b) -> {
                Double scoreA = (Double) a.get("matchScore");
                Double scoreB = (Double) b.get("matchScore");
                return scoreB.compareTo(scoreA);
            });

            return R.success(matchedPets);
        } catch (Exception e) {
            log.error("获取匹配宠物列表异常: {}", e.getMessage(), e);
            return R.error("获取匹配宠物列表失败");
        }
    }

    /**
     * 计算用户和宠物的匹配分数
     * 
     * @param userId 用户ID
     * @param petId  宠物ID
     * @return 匹配分数和匹配详情
     */
    @Override
    public R<Map<String, Object>> calculateMatchScore(Integer userId, Integer petId) {
        try {
            // 获取用户性格特质
            LambdaQueryWrapper<UserPersonality> userQuery = new LambdaQueryWrapper<>();
            userQuery.eq(UserPersonality::getUserId, userId);
            List<UserPersonality> userTraits = userPersonalityMapper.selectList(userQuery);

            if (userTraits.isEmpty()) {
                return R.error("用户未设置性格特质");
            }

            // 获取宠物性格特质
            LambdaQueryWrapper<PetPersonality> petQuery = new LambdaQueryWrapper<>();
            petQuery.eq(PetPersonality::getPetId, petId);
            List<PetPersonality> petTraits = petPersonalityMapper.selectList(petQuery);

            if (petTraits.isEmpty()) {
                return R.error("宠物未设置性格特质");
            }

            // 计算匹配分数
            double totalScore = 0;
            List<Map<String, Object>> matchDetails = new ArrayList<>();

            for (UserPersonality userTrait : userTraits) {
                for (PetPersonality petTrait : petTraits) {
                    // 查找这两个特质的兼容性
                    LambdaQueryWrapper<PersonalityCompatibility> compatQuery = new LambdaQueryWrapper<>();
                    compatQuery.eq(PersonalityCompatibility::getTrait1Id, userTrait.getTraitId())
                            .eq(PersonalityCompatibility::getTrait2Id, petTrait.getTraitId());
                    PersonalityCompatibility compatibility = compatibilityMapper.selectOne(compatQuery);

                    if (compatibility != null) {
                        // 计算这对特质的匹配分数
                        double traitScore = (compatibility.getCompatibilityScore() / 10.0) *
                                (userTrait.getTraitValue() / 10.0) *
                                (petTrait.getTraitValue() / 10.0);

                        totalScore += traitScore;

                        // 获取特质名称
                        PersonalityTrait userTraitObj = traitMapper.selectById(userTrait.getTraitId());
                        PersonalityTrait petTraitObj = traitMapper.selectById(petTrait.getTraitId());

                        // 添加匹配详情
                        Map<String, Object> detail = new HashMap<>();
                        detail.put("userTrait", userTraitObj.getTraitName());
                        detail.put("petTrait", petTraitObj.getTraitName());
                        detail.put("compatibility", compatibility.getCompatibilityScore());
                        detail.put("score", traitScore);
                        detail.put("description", compatibility.getDescription());
                        matchDetails.add(detail);
                    }
                }
            }

            // 归一化分数到0-100
            double normalizedScore = Math.min(100, totalScore * 10);

            Map<String, Object> result = new HashMap<>();
            result.put("score", normalizedScore);
            result.put("details", matchDetails);

            return R.success(result);
        } catch (Exception e) {
            log.error("计算匹配分数异常: {}", e.getMessage(), e);
            return R.error("计算匹配分数失败");
        }
    }

    /**
     * 保存用户性格特质
     * 
     * @param userId 用户ID
     * @param traits 特质ID和值的映射
     * @return 操作结果
     */
    @Override
    @Transactional
    public R<String> saveUserPersonality(Integer userId, Map<Integer, Integer> traits) {
        try {
            // 先删除用户现有的性格特质
            LambdaQueryWrapper<UserPersonality> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserPersonality::getUserId, userId);
            userPersonalityMapper.delete(queryWrapper);

            // 保存新的性格特质
            for (Map.Entry<Integer, Integer> entry : traits.entrySet()) {
                UserPersonality personality = new UserPersonality();
                personality.setUserId(userId);
                personality.setTraitId(entry.getKey());
                personality.setTraitValue(entry.getValue());
                userPersonalityMapper.insert(personality);
            }

            return R.success("保存用户性格特质成功");
        } catch (Exception e) {
            log.error("保存用户性格特质异常: {}", e.getMessage(), e);
            return R.error("保存用户性格特质失败");
        }
    }

    /**
     * 保存宠物性格特质
     * 
     * @param petId  宠物ID
     * @param traits 特质ID和值的映射
     * @return 操作结果
     */
    @Override
    @Transactional
    public R<String> savePetPersonality(Integer petId, Map<Integer, Integer> traits) {
        try {
            // 先删除宠物现有的性格特质
            LambdaQueryWrapper<PetPersonality> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(PetPersonality::getPetId, petId);
            petPersonalityMapper.delete(queryWrapper);

            // 保存新的性格特质
            for (Map.Entry<Integer, Integer> entry : traits.entrySet()) {
                PetPersonality personality = new PetPersonality();
                personality.setPetId(petId);
                personality.setTraitId(entry.getKey());
                personality.setTraitValue(entry.getValue());
                petPersonalityMapper.insert(personality);
            }

            return R.success("保存宠物性格特质成功");
        } catch (Exception e) {
            log.error("保存宠物性格特质异常: {}", e.getMessage(), e);
            return R.error("保存宠物性格特质失败");
        }
    }
}
