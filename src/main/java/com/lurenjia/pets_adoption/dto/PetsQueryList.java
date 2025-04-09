package com.lurenjia.pets_adoption.dto;

import com.lurenjia.pets_adoption.entity.Pets;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xuanzhen
 * @date 2024/12/21-12:30
 * @description 宠物页码查询条件
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetsQueryList {
    private Integer page;
    private Integer pageSize;
    private Pets pets;
    private Integer userType;

    /**
     * toString：1_5_1
     * @return 页码_页码大小_用户类型(1 管理员，0 非管理员)
     */
    @Override
    public String toString(){
        return page+"_"+pageSize+"_"+userType;
    }
}
