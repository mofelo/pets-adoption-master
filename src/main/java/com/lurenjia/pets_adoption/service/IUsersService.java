package com.lurenjia.pets_adoption.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lurenjia.pets_adoption.dto.R;
import com.lurenjia.pets_adoption.entity.Users;

import javax.servlet.http.HttpSession;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lurenjia
 * @since 2023-03-25
 */
public interface IUsersService extends IService<Users> {

    /**
     * 通过手机验证码登录系统
     * @param phone
     * @param code
     * @param session
     * @return
     */
    R<String> loginByPhone(String phone, String code, HttpSession session);

    /**
     * 发送短信验证码
     * @param phone
     * @param session
     * @return
     */
    R<String> sendMsg(String phone, HttpSession session);

    /**
     * 账户密码登录
     * @param users
     * @param session
     * @return
     */
    R<String> login(Users users, HttpSession session);

    /**
     * 注册账户
     * @param users
     * @return
     */
    R<String> logon(Users users);

    /**
     * 获取列表信息
     * @param page 页码
     * @param pageSize 页码大小
     * @param user 查询条件
     * @return
     */
    R<Page<Users>> getList(Integer page, Integer pageSize, Users user);

    /**
     * 修改用户类型
     * @param id
     * @param code
     * @return
     */
    R<String> updateUserField(Long id, Integer code);
}
