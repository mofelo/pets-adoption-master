package com.lurenjia.pets_adoption.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lurenjia.pets_adoption.dto.R;
import com.lurenjia.pets_adoption.entity.Adoptions;
import com.lurenjia.pets_adoption.entity.Notices;
import com.lurenjia.pets_adoption.entity.Pets;
import com.lurenjia.pets_adoption.entity.Users;
import com.lurenjia.pets_adoption.mapper.UsersMapper;
import com.lurenjia.pets_adoption.service.IAdoptionsService;
import com.lurenjia.pets_adoption.service.INoticesService;
import com.lurenjia.pets_adoption.service.IPetsService;
import com.lurenjia.pets_adoption.service.IUsersService;
import com.lurenjia.pets_adoption.utils.SendSmsUtils;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lurenjia
 * @since 2023-03-25
 */
@Service
@Slf4j
@EnableScheduling
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {

    @Autowired
    @Lazy
    private IAdoptionsService adoptionsService;

    @Autowired
    @Lazy
    private IPetsService petsService;

    @Autowired
    @Lazy
    private INoticesService noticesService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public R<String> loginByPhone(String phone, String code, HttpSession session) {
        try {
            // 取验证码 redis中
            String cacheCode = null;
            try {
                cacheCode = stringRedisTemplate.opsForValue().get("login:" + phone);
            } catch (Exception e) {
                log.error("获取Redis验证码异常: {}", e.getMessage(), e);
                // Redis异常时，允许使用任意验证码登录（仅用于测试）
                cacheCode = code;
            }

            if (cacheCode == null) {
                return R.error("验证码已失效，请重新获取！");
            }
            // 判断 验证码错误
            if (!cacheCode.equals(code)) {
                return R.error("验证码错误,请确认输入！");
            }

            // 取用户信息 数据库中
            LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Users::getUserPhone, phone);
            Users user = getOne(queryWrapper);
            if (user == null) {
                return R.error("手机号未注册，请先注册账户！");
            }
            // 判断 账户状态
            if (user.getUserStatus() != null && user.getUserStatus() == 3) {
                return R.error("账户已暂停使用，详细信息请联系管理员！");
            }
            // 登录成功，账户存入session中，保持连接
            session.setAttribute("user", user);

            // 同时存入userId和userType，便于其他功能使用
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("userType", user.getUserType());

            // 删除redis中的验证码
            try {
                stringRedisTemplate.delete("login:" + phone);
            } catch (Exception e) {
                log.error("删除Redis验证码异常: {}", e.getMessage(), e);
                // 忽略Redis异常
            }

            log.info("用户 {} 登录成功！", user.getUserName());
            return R.success("登录成功！");
        } catch (Exception e) {
            log.error("手机验证码登录异常: {}", e.getMessage(), e);
            return R.error("登录异常，请稍后重试");
        }
    }

    @Override
    public R<String> sendMsg(String phone, HttpSession session) {
        // 1 取出手机号绑定的用户 数据库中
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Users::getUserPhone, phone);
        Users one = this.getOne(queryWrapper);
        // 1.5 判断 账户存在
        if (one == null) {
            return R.error("手机号未注册或绑定,请更换登录方式！");
        }
        // 2 判断 账户状态
        if (one.getUserStatus() == 3) {
            return R.error("账户已暂停使用，详细信息请联系管理员！");
        }
        // 3 生成 随机的6位数字验证码
        String code = RandomUtil.randomNumbers(6);
        // 4 发送 验证码到手机
        String message = SendSmsUtils.sendCode(code, phone);
        if ("OK".equals(message)) {
            // 5 验证码存入 Redis中，key为指定前缀+手机号,有效期为5分钟
            stringRedisTemplate.opsForValue().set("login:" + phone, code, 5, TimeUnit.MINUTES);
            return R.success(message);
        }
        // 5 验证码发送 失败，返回失败信息
        return R.error(message);
    }

    /**
     * 账户密码登录
     *
     * @param users
     * @param session
     * @return
     */
    @Override
    public R<String> login(Users users, HttpSession session) {
        try {
            // 输入验证
            if (users.getUserAccount() == null || users.getUserAccount().isEmpty()) {
                return R.error("请输入用户名");
            }
            if (users.getUserPassword() == null || users.getUserPassword().isEmpty()) {
                return R.error("请输入密码");
            }

            // 1 取出账户信息 从数据库中
            LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Users::getUserAccount, users.getUserAccount());
            Users user = this.getOne(queryWrapper);
            // 2 判断 账户存在
            if (user == null) {
                return R.error("账户不存在，请检查输入或注册新账户！");
            }
            // 3 判断 账户状态
            if (user.getUserStatus() != null && user.getUserStatus() == 3) {
                return R.error("账户已暂停使用，详细信息请联系管理员！");
            }
            // 4 判断 账户密码是否正确
            if (users.getUserPassword().equals(user.getUserPassword())) {
                // 正确：账户存入session中，保持连接,30分钟
                session.setAttribute("user", user);

                // 同时存入userId和userType，便于其他功能使用
                session.setAttribute("userId", user.getUserId());
                session.setAttribute("userType", user.getUserType());

                log.info("用户 {} 登录成功！", user.getUserName());
                return R.success("登录成功");
            } else {
                // 错误
                return R.error("密码错误,请检查输入！");
            }
        } catch (Exception e) {
            log.error("登录异常: {}", e.getMessage(), e);
            return R.error("登录异常，请稍后重试");
        }
    }

    /**
     * 注册账户
     *
     * @param users
     * @return
     */
    @Override
    public R<String> logon(Users users) {
        // 1 取出账户信息 从数据库
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Users::getUserAccount, users.getUserAccount());
        Users userOne = this.getOne(queryWrapper);
        // 2 判断 账户名已存在
        if (userOne != null) {
            return R.error("此账户名已经存在，换一个吧~");
        }
        // 3 设置 必须的初始值
        {
            // 注册时间
            users.setUserRegistertime(LocalDate.now());
            // 昵称，默认和账户一样
            users.setUserName(users.getUserAccount());
            // 性别，2代表未知
            users.setUserSex(2);
            // 状态，0表示正常
            users.setUserStatus(0);
            // 类型，0表示普通用户
            users.setUserType(0);
        }
        // 4 存入账户信息 到数据库中
        this.save(users);
        return R.success("注册成功!");
    }

    /**
     * 获取列表信息
     *
     * @param page     页码
     * @param pageSize 页码大小
     * @param user     查询条件
     * @return
     */
    @Override
    public R<Page<Users>> getList(Integer page, Integer pageSize, Users user) {
        // 1 构建 查询条件对象
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        // 请求携带查询条件：用户姓名、用户状态，可能为空
        queryWrapper.like((user.getUserRealname() != null) && !("".equals(user.getUserRealname())),
                Users::getUserRealname, user.getUserRealname());
        queryWrapper.eq(user.getUserStatus() != null,
                Users::getUserStatus, user.getUserStatus());
        queryWrapper.orderByDesc(Users::getUserRegistertime);

        // 2 准备 页面数据对象
        Page<Users> pageInfo = new Page<>(page, pageSize);
        // 3 查询数据
        this.page(pageInfo, queryWrapper);
        // 4 响应数据
        return R.success(pageInfo);
    }

    /**
     * 修改用户类型：置为管理员、取消管理员
     *
     * @param id   用户编号
     * @param code 1表示修改账户类型，2表示修改账户状态
     * @return
     */
    @Override
    public R<String> updateUserField(Long id, Integer code) {
        // 1 获取要修改的用户信息 从数据库中
        Users users = this.getById(id);
        // 2 判断 用户存在
        if (users == null) {
            return R.error("操作错误，请重试！");
        }

        // 3 判断 修改操作类型
        if (code == 1) {
            log.info("账户类型修改之前,类型为：{}", users.getUserType());
            // 用户类型
            users = updateUserType(users);
        } else if (code == 2) {
            log.info("账户状态修改之前,状态为：{}", users.getUserStatus());
            // 用户状态
            users = updateUserStatus(users);
        }

        log.info("账户修改之后为：{}", users);
        return R.success("修改成功");
    }

    /**
     * 修改账户状态：暂停使用、恢复使用
     * <p>
     * 0正常，1申请领养中，2异常状态，3暂停使用
     *
     * @param users
     * @return
     */
    @CacheEvict(value = "adoption", key = "'error_adopter_name'")
    @Transactional
    public Users updateUserStatus(Users users) {
        // 获取 账户类型
        Integer userStatus = users.getUserStatus();

        switch (userStatus) {
            // 申请领养中用户
            case 1:
                // 获取 申领中的记录
                LambdaQueryWrapper<Adoptions> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(Adoptions::getUserId, users.getUserId());
                queryWrapper.eq(Adoptions::getAdoStatus, 2);
                Adoptions adoptions = adoptionsService.getOne(queryWrapper);

                // 设置 领养状态为失败
                adoptions.setAdoStatus(0);
                adoptions.setAdoNote("用户被冻结，领养失败。");
                // 更新领养状态 到数据库中
                adoptionsService.updateById(adoptions);

                // 设置 宠物状态为待领养
                Pets pets = petsService.getById(adoptions.getPetId());
                pets.setPetStatus(0);
                // 更新宠物状态 到数据库中
                petsService.updateById(pets);
                // 正常用户，直接设置为冻结
            case 0:
                // 异常状态的领养人，直接设置为冻结
            case 2:
                users.setUserStatus(3);
                break;
            // 暂停使用的账户，恢复使用
            case 3:
                users.setUserStatus(0);
                break;
            default:
        }
        // 更新用户信息 到数据库
        this.updateById(users);
        return users;
    }

    /**
     * 更新领养人状态：
     * 内容：判断领养人是否领养一年内，三个月内没有发布领养日志，如果是，则设置用户状态异常。
     * 定时执行：每天23：59：59进行执行
     * 执行后清除缓存中的异常领养人数据
     */
    @Scheduled(cron = "59 59 23 * * ?")
    @CacheEvict(value = "adoption", key = "'error_adopter_name'")
    public void userStatusUpdate() {
        log.info("时辰已到！开始更新领养人状态！");
        // 获取所有的领养成功的记录
        LambdaQueryWrapper<Adoptions> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Adoptions::getAdoStatus, 1);
        List<Adoptions> adoptionsList = adoptionsService.list(queryWrapper);
        log.info("领养记录：{}条", Integer.valueOf(adoptionsList.size()));

        // 获取当前日期
        LocalDate nowDate = LocalDate.now();
        // 三个月前的时间
        LocalDate nowAfter3 = nowDate.minusMonths(3);

        // 遍历领养记录
        for (Adoptions adoptions : adoptionsList) {
            log.info("开始处理领养记录，编号：{}", adoptions.getAdoId());

            // 领养日期
            LocalDate adoptionDate = adoptions.getAdoDate();
            // 领养日期的三个月之后
            LocalDate adoptionAfter3Month = adoptionDate.minusMonths(-3);

            // 领养日期之后的一年
            LocalDate adoptionAfter1YearDate = adoptionDate.minusYears(-1);

            // 如果当前时间是领养一年以后，则不需要进行操作
            if (nowDate.isAfter(adoptionAfter1YearDate)) {
                log.info("此记录已经过了一年，不需要处理：{}", adoptions.getAdoId());
                continue;
            }

            // 领养人
            Users user = this.getById(adoptions.getUserId());

            // 如果领养人账户已经被冻结，则不操作
            if (user.getUserStatus() == 3) {
                log.info("此记录领养人被冻结了，不需要处理。{}", adoptions.getAdoId());
                continue;
            }

            // 获取领养人的所有领养日志
            LambdaQueryWrapper<Notices> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Notices::getUserId, (adoptions.getUserId()));
            wrapper.eq(Notices::getNoticeType, 2);
            List<Notices> noticesList = noticesService.list(wrapper);

            // 没有发布领养日志
            if (noticesList == null || noticesList.size() == 0) {
                // 判断今天是否为领养三个月后
                if (nowDate.isAfter(adoptionAfter3Month)) {
                    // 设置此人状态异常
                    user.setUserStatus(2);
                    this.updateById(user);
                    log.info("用户{}未在三个月内发布领养日志，设置为异常.", user.getUserRealname());
                    continue;
                } else {
                    // 时间未到
                    log.info("用户{}领养后还未到三个月，正常状态.", user.getUserRealname());
                    continue;
                }
            }

            // 获取最近一篇领养日志的发布时间
            Notices notices = noticesList.get(noticesList.size() - 1);
            LocalDate noticeDate = notices.getNoticeDate();

            // 最近一篇领养日志的发布时间，三个月后
            LocalDate noticeAfter3Months = noticeDate.minusMonths(-3);

            // 判断今天是否为发布后的三个月后
            if (nowDate.isAfter(noticeAfter3Months)) {
                // 设置此人状态异常
                user.setUserStatus(2);
                this.updateById(user);
                log.info("用户{}未在一年内三个月内发布领养日志，设置为异常.", user.getUserRealname());
                continue;
            } else {
                log.info("用户{}距离上一次发布领养日志不到三个月，正常状态.", user.getUserRealname());
            }
        }
        log.info("领养人状态更新完毕");
    }

    /**
     * 修改账户类型：1管理员、2领养人、0普通用户
     *
     * @param users
     * @return
     */
    private Users updateUserType(Users users) {
        // 1 获取账户类型
        Integer userType = users.getUserType();

        // 2 判断账户类型
        switch (userType) {
            // 是管理员用户，则取消管理员，置为普通用户或领养人
            case 1:
                // 获取 此账户领养成功的记录 从数据库中
                LambdaQueryWrapper<Adoptions> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(Adoptions::getUserId, users.getUserId());
                queryWrapper.eq(Adoptions::getAdoStatus, 1);
                List<Adoptions> list = adoptionsService.list(queryWrapper);

                // 判断 有领养成功记录
                if (list.size() > 0) {
                    // 置为领养人
                    users.setUserType(2);
                } else {
                    // 置为普通用户
                    users.setUserType(0);
                }
                break;
            // 不是管理员，设置为管理员
            default:
                users.setUserType(1);
        }
        // 3 更新用户信息 到数据库
        this.updateById(users);
        return users;
    }
}
