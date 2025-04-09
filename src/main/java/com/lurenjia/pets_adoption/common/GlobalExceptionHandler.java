package com.lurenjia.pets_adoption.common;

import com.lurenjia.pets_adoption.dto.R;
import com.lurenjia.pets_adoption.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.SQLException;

/**
 * 作者： lurenjia
 * 时间： 2023/3/16-15:50
 * 描述： 全局异常处理器，截获指定控制器类抛出的异常，底层使用aop
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 截获所有的sql异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(SQLException.class)
    public R<String> exceptionHandler(SQLException e) {
        log.info("捕获到了SQL异常：{}", e.getMessage());
        if (e.getMessage().contains("Duplicate entry")) {
            String[] split = e.getMessage().split(" ");
            //获取到重复的字段值
            String msg = split[2] + "已存在";
            return R.error(msg);
        }

        if (e.getMessage().contains("Data truncation: Data too long")) {
            String[] split = e.getMessage().split(" ");
            //数据长度超出限制
            String msg = "输入数据过长，请检查输入";
            return R.error(msg);
        }
        return R.error("未知错误");
    }

    /**
     * 捕获自定义的业务异常
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public R<String> serviceHandler(CustomException e){
        log.info("捕获到了业务异常：{}", e.getMessage());
        return R.error(e.getMessage());
    }
    /**
     * 捕获io操作的业务异常
     * @return
     */
    @ExceptionHandler(IOException.class)
    public R<String> ioHandler(IOException e){
        log.info("捕获到了文件操作异常：{}", e.getMessage());
        return R.error(e.getMessage());
    }
}
