package com.lurenjia.pets_adoption.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lurenjia.pets_adoption.dto.R;
import com.lurenjia.pets_adoption.entity.Adoptions;
import com.lurenjia.pets_adoption.entity.Pets;
import com.lurenjia.pets_adoption.service.IAdoptionsService;
import com.lurenjia.pets_adoption.service.IPetsService;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 统计数据控制器
 * </p>
 *
 * @author lurenjia
 * @since 2023-03-25
 */
@RestController
@RequestMapping("/pets")
@Slf4j
public class StatisticsController {

    @Autowired
    private IPetsService petsService;

    @Autowired
    private IAdoptionsService adoptionsService;

    /**
     * 获取统计数据
     * 
     * @return 统计数据
     */
    @GetMapping("/statistics")
    public R<Map<String, Object>> getStatistics() {
        log.info("获取宠物统计数据");
        Map<String, Object> resultMap = new HashMap<>();

        try {
            // 获取所有宠物数据
            List<Pets> petsList = petsService.list();

            // 宠物类型分布
            Map<String, Long> petTypeMap = petsList.stream()
                    .collect(Collectors.groupingBy(Pets::getPetType, Collectors.counting()));
            List<Map<String, Object>> petTypeData = convertToChartData(petTypeMap);
            resultMap.put("petTypeData", petTypeData);

            // 宠物状态分布
            Map<Integer, Long> petStatusMap = petsList.stream()
                    .collect(Collectors.groupingBy(Pets::getPetStatus, Collectors.counting()));
            List<Map<String, Object>> petStatusData = new ArrayList<>();
            for (Map.Entry<Integer, Long> entry : petStatusMap.entrySet()) {
                Map<String, Object> item = new HashMap<>();
                item.put("name", formatPetStatus(entry.getKey()));
                item.put("value", entry.getValue());
                petStatusData.add(item);
            }
            resultMap.put("petStatusData", petStatusData);

            // 宠物性别分布
            Map<Integer, Long> petSexMap = petsList.stream()
                    .collect(Collectors.groupingBy(Pets::getPetSex, Collectors.counting()));
            List<Map<String, Object>> petSexData = new ArrayList<>();
            for (Map.Entry<Integer, Long> entry : petSexMap.entrySet()) {
                Map<String, Object> item = new HashMap<>();
                item.put("name", entry.getKey() == 1 ? "雄性" : "雌性");
                item.put("value", entry.getValue());
                petSexData.add(item);
            }
            resultMap.put("petSexData", petSexData);

            // 宠物性格分布
            Map<Integer, Long> petPersonalityMap = petsList.stream()
                    .collect(Collectors.groupingBy(Pets::getPetPersonality, Collectors.counting()));
            List<Map<String, Object>> petPersonalityData = new ArrayList<>();
            for (Map.Entry<Integer, Long> entry : petPersonalityMap.entrySet()) {
                Map<String, Object> item = new HashMap<>();
                item.put("name", entry.getKey() == 1 ? "外向" : "内向");
                item.put("value", entry.getValue());
                petPersonalityData.add(item);
            }
            resultMap.put("petPersonalityData", petPersonalityData);

            // 宠物年龄分布
            List<Map<String, Object>> petAgeData = calculatePetAgeDistribution(petsList);
            resultMap.put("petAgeData", petAgeData);

            // 月度领养趋势
            Map<String, Object> adoptionTrendData = calculateAdoptionTrend();
            resultMap.put("adoptionTrendData", adoptionTrendData);

            return R.success(resultMap);
        } catch (Exception e) {
            log.error("获取统计数据出错", e);
            return R.error("获取统计数据出错: " + e.getMessage());
        }
    }

    /**
     * 计算宠物年龄分布
     */
    private List<Map<String, Object>> calculatePetAgeDistribution(List<Pets> petsList) {
        Map<String, Integer> ageDistribution = new HashMap<>();
        ageDistribution.put("1岁以下", 0);
        ageDistribution.put("1-3岁", 0);
        ageDistribution.put("3-5岁", 0);
        ageDistribution.put("5-8岁", 0);
        ageDistribution.put("8岁以上", 0);

        int currentYear = LocalDate.now().getYear();

        for (Pets pet : petsList) {
            try {
                LocalDate birthDate = pet.getPetAge();
                if (birthDate == null) {
                    continue;
                }
                int birthYear = birthDate.getYear();
                int age = currentYear - birthYear;

                if (age < 1) {
                    ageDistribution.put("1岁以下", ageDistribution.get("1岁以下") + 1);
                } else if (age >= 1 && age < 3) {
                    ageDistribution.put("1-3岁", ageDistribution.get("1-3岁") + 1);
                } else if (age >= 3 && age < 5) {
                    ageDistribution.put("3-5岁", ageDistribution.get("3-5岁") + 1);
                } else if (age >= 5 && age < 8) {
                    ageDistribution.put("5-8岁", ageDistribution.get("5-8岁") + 1);
                } else {
                    ageDistribution.put("8岁以上", ageDistribution.get("8岁以上") + 1);
                }
            } catch (Exception e) {
                // 处理日期格式异常
                log.warn("宠物年龄格式异常: {}", pet.getPetAge());
            }
        }

        return convertToChartData(ageDistribution);
    }

    /**
     * 计算月度领养趋势
     */
    private Map<String, Object> calculateAdoptionTrend() {
        // 获取所有已完成的领养记录
        LambdaQueryWrapper<Adoptions> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Adoptions::getAdoStatus, 1); // 已完成的领养
        List<Adoptions> adoptionsList = adoptionsService.list(queryWrapper);

        // 获取当前年份
        int currentYear = LocalDate.now().getYear();

        // 初始化月度数据
        String[] months = { "1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月" };
        Integer[] adoptionCounts = new Integer[12];
        Arrays.fill(adoptionCounts, 0);

        // 统计每月领养数量
        for (Adoptions adoption : adoptionsList) {
            try {
                LocalDate adoptionDate = adoption.getAdoDate();
                if (adoptionDate == null) {
                    continue;
                }
                // 只统计当年的数据
                if (adoptionDate.getYear() == currentYear) {
                    int month = adoptionDate.getMonthValue();
                    adoptionCounts[month - 1]++;
                }
            } catch (Exception e) {
                // 处理日期格式异常
                log.warn("领养日期处理异常: {}", e.getMessage());
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("months", months);
        result.put("data", adoptionCounts);
        return result;
    }

    /**
     * 将Map转换为图表数据格式
     */
    private <T> List<Map<String, Object>> convertToChartData(Map<T, ?> dataMap) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<T, ?> entry : dataMap.entrySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", entry.getKey().toString());
            item.put("value", entry.getValue());
            result.add(item);
        }
        return result;
    }

    /**
     * 格式化宠物状态
     */
    private String formatPetStatus(Integer status) {
        switch (status) {
            case 0:
                return "待领养";
            case 1:
                return "已被领养";
            case 2:
                return "被申领";
            case 3:
                return "离世";
            default:
                return "未知状态";
        }
    }
}