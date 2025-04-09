package com.lurenjia.pets_adoption.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lurenjia.pets_adoption.dto.R;
import com.lurenjia.pets_adoption.entity.Users;
import com.lurenjia.pets_adoption.service.IPersonalityMatchService;
import com.lurenjia.pets_adoption.service.IPersonalityTraitService;

import lombok.extern.slf4j.Slf4j;

/**
 * 性格匹配Controller
 */
@RestController
@RequestMapping("/personality")
@Slf4j
public class PersonalityMatchController {

    @Autowired
    private IPersonalityMatchService personalityMatchService;

    @Autowired
    private IPersonalityTraitService personalityTraitService;

    /**
     * 获取所有性格特质
     * 
     * @return 性格特质列表
     */
    @GetMapping("/traits")
    public R<List<?>> getAllTraits() {
        return R.success(personalityTraitService.list());
    }

    /**
     * 保存用户性格特质
     * 
     * @param traits  特质ID和值的映射
     * @param session 会话
     * @return 操作结果
     */
    @PostMapping("/user/save")
    public R<String> saveUserPersonality(@RequestBody Map<Integer, Integer> traits, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return R.error("用户未登录");
        }

        log.info("保存用户性格特质: userId={}, traits={}", user.getUserId(), traits);
        return personalityMatchService.saveUserPersonality(user.getUserId().intValue(), traits);
    }

    /**
     * 保存宠物性格特质
     * 
     * @param petId  宠物ID
     * @param traits 特质ID和值的映射
     * @return 操作结果
     */
    @PostMapping("/pet/save/{petId}")
    public R<String> savePetPersonality(@PathVariable Integer petId, @RequestBody Map<Integer, Integer> traits) {
        log.info("保存宠物性格特质: petId={}, traits={}", petId, traits);
        return personalityMatchService.savePetPersonality(petId, traits);
    }

    /**
     * 获取匹配的宠物列表
     * 
     * @param session 会话
     * @return 匹配的宠物列表
     */
    @GetMapping("/match")
    public R<List<Map<String, Object>>> getMatchedPets(HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return R.error("用户未登录");
        }

        log.info("获取匹配宠物列表: userId={}", user.getUserId());
        return personalityMatchService.getMatchedPets(user.getUserId().intValue());
    }

    /**
     * 计算用户和宠物的匹配分数
     * 
     * @param petId   宠物ID
     * @param session 会话
     * @return 匹配分数和匹配详情
     */
    @GetMapping("/match/{petId}")
    public R<Map<String, Object>> calculateMatchScore(@PathVariable Integer petId, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return R.error("用户未登录");
        }

        log.info("计算匹配分数: userId={}, petId={}", user.getUserId(), petId);
        return personalityMatchService.calculateMatchScore(user.getUserId().intValue(), petId);
    }
}
