package com.lurenjia.pets_adoption.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 宠物类型验证工具类
 * 用于验证宠物类型是否为猫或狗
 */
public class PetTypeValidator {
    
    // 允许的猫的品种集合
    private static final Set<String> CAT_BREEDS = new HashSet<>(Arrays.asList(
            "家猫", "波斯猫", "英国短毛猫", "美国短毛猫", "暹罗猫", "缅因猫", "布偶猫", "俄罗斯蓝猫",
            "埃及猫", "孟买猫", "挪威森林猫", "理花猫", "斯芬克斯猫", "苏格兰折耳猫", "异国短毛猫", "中华田园猫"
    ));
    
    // 允许的狗的品种集合
    private static final Set<String> DOG_BREEDS = new HashSet<>(Arrays.asList(
            "田园犬", "柴犬", "拉布拉多犬", "金毛犬", "哈士奇", "边境牧羊犬", "萨摩耶", "德国牧羊犬",
            "比熊犬", "泰迪犬", "博美犬", "巴哥犬", "吉娃娃", "斗牛犬", "杜宾犬", "大丹犬", "松狮犬"
    ));
    
    /**
     * 验证宠物类型是否为猫或狗
     * @param petType 宠物类型
     * @return 如果是猫或狗返回true，否则返回false
     */
    public static boolean isValidPetType(String petType) {
        if (petType == null || petType.trim().isEmpty()) {
            return false;
        }
        
        return CAT_BREEDS.contains(petType) || DOG_BREEDS.contains(petType);
    }
    
    /**
     * 获取所有有效的宠物类型列表
     * @return 所有有效的宠物类型集合
     */
    public static Set<String> getAllValidPetTypes() {
        Set<String> allTypes = new HashSet<>(CAT_BREEDS);
        allTypes.addAll(DOG_BREEDS);
        return allTypes;
    }
    
    /**
     * 判断是否为猫
     * @param petType 宠物类型
     * @return 如果是猫返回true，否则返回false
     */
    public static boolean isCat(String petType) {
        return CAT_BREEDS.contains(petType);
    }
    
    /**
     * 判断是否为狗
     * @param petType 宠物类型
     * @return 如果是狗返回true，否则返回false
     */
    public static boolean isDog(String petType) {
        return DOG_BREEDS.contains(petType);
    }
}