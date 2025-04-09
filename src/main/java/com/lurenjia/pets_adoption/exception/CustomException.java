package com.lurenjia.pets_adoption.exception;

/**
 * 作者： xuanzhen
 * 时间： 2023/3/18-12:27
 * 描述： 自定义的业务异常
 */
public class CustomException extends RuntimeException{
    /**
     * 错误信息
     * @param msg
     */
    public CustomException(String msg) {
        super(msg);
    }
}
