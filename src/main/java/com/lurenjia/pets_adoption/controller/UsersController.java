package com.lurenjia.pets_adoption.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lurenjia.pets_adoption.dto.R;
import com.lurenjia.pets_adoption.entity.Users;
import com.lurenjia.pets_adoption.service.IUsersService;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lurenjia
 * @since 2023-03-25
 */
@RestController
@RequestMapping("/users")
@Slf4j
public class UsersController {

    @Autowired
    private IUsersService usersService;

    /**
     * 通过验证码登录
     * 
     * @param phone
     * @param code
     * @param session
     * @return
     */
    @GetMapping("/loginByPhone")
    public R<String> loginByPhone(String phone, String code, HttpSession session) {
        log.info("登录手机号：{} 验证码：{}", phone, code);
        R<String> resp = usersService.loginByPhone(phone, code, session);
        return resp;
    }

    /**
     * 获取手机验证码
     * 
     * @param phone
     * @return
     */
    @GetMapping("/sendMsg")
    public R<String> sendMsg(String phone, HttpSession session) {
        log.info("申请验证码的手机号：{}", phone);
        R<String> resp = usersService.sendMsg(phone, session);
        return resp;
    }

    /**
     * 获取当前操作用户类型
     *
     * @param session
     * @return
     */
    @GetMapping("/type")
    public R<Integer> type(HttpSession session) {
        log.info("获取当前操作用户类型");
        Users user = (Users) session.getAttribute("user");
        Integer type = (user.getUserType() == null) ? 0 : user.getUserType();
        return R.success(type);
    }

    /**
     * 获取当前用户信息
     * 
     * @return
     */
    @GetMapping
    public R<Users> users(HttpSession session) {
        log.info("获取当前操作用户信息");
        Users user = (Users) session.getAttribute("user");

        if (user != null) {
            return R.success(user);
        }
        return R.error("没有登录嗷嗷~");
    }

    /**
     * 账户密码登录
     *
     * @param users
     * @return
     */
    @PostMapping("/login")
    public R<String> login(@RequestBody Users users, HttpSession session) {
        log.info("请求登录用户为：{}", users.getUserAccount());
        R<String> resp = usersService.login(users, session);
        return resp;
    }

    /**
     * 注册操作
     *
     * @return
     */
    @PostMapping("/logon")
    public R<String> lonon(@RequestBody Users users) {
        log.info("请求注册用户为：{}", users);
        R<String> resp = usersService.logon(users);
        return resp;
    }

    /**
     * 登出操作
     *
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpSession session) {
        log.info("{} 退出登录!", ((Users) session.getAttribute("user")).getUserAccount());
        // session中移除用户id
        session.removeAttribute("user");
        return R.success("退出成功");
    }

    /**
     * 获取用户列表信息
     *
     * @param page
     * @param pageSize
     * @param user
     * @return
     */
    @GetMapping("/page")
    public R<Page<Users>> page(Integer page, Integer pageSize, Users user) {
        log.info("收到获取用户列表的请求,查询条件为：{}", user);
        R<Page<Users>> resp = usersService.getList(page, pageSize, user);
        return resp;
    }

    /**
     * 修改用户类型或状态
     * 
     * @return
     */
    @PutMapping("/{id}/{code}")
    public R<String> update(@PathVariable Long id, @PathVariable Integer code) {
        log.info("收到请求，要修改的用户编号：{}", id);
        R<String> resp = usersService.updateUserField(id, code);
        return resp;
    }

    /**
     * 修改账户信息
     * 
     * @return
     */
    @PutMapping
    public R<String> updateUser(@RequestBody Users user, HttpSession session) {
        log.info("收到请求，修改账户信息：{}", user);
        // 修改账户信息 到数据库
        usersService.updateById(user);
        // 更新用户信息 session中的
        session.setAttribute("user", user);
        return R.success("修改成功！");
    }
}
