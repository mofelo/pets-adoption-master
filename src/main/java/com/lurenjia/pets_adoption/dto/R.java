package com.lurenjia.pets_adoption.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 作者： xuanzhen
 * 时间： 2024/12/16-11:10
 * 描述： 响应数据格式
 */
@Data
public class R<T> implements Serializable {

    /**
     * 响应状态码:1成功，0失败
     */
    private Integer code;

    /**
     * 错误消息
     */
    private String msg;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 响应成功直接调用此方法，传入需要放回的数据即可（data）
     */
    public static <T> R<T> success(T object) {
        R<T> r = new R<T>();
        r.data = object;
        r.code = 1;
        return r;
    }

    /**
     * 响应失败直接调用此方法，传入需要响应的提示信息即可（msg）
     */
    public static <T> R<T> error(String msg) {
        R r = new R();
        r.msg = msg;
        r.code = 0;
        return r;
    }

}