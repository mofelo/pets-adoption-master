package com.lurenjia.pets_adoption.dto;

import lombok.Data;

import java.time.LocalDateTime;
/**
 * @author xuanzhen
 * @date 2024/12/21-11:54
 * @description 带有逻辑过期时间的缓存对象
 */
@Data
public class RedisData{
    /**
     * 逻辑过期时间
     */
    private LocalDateTime expireTime;
    /**
     * 缓存数据
     */
    private Object data;
}