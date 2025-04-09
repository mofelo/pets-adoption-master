package com.lurenjia.pets_adoption.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lurenjia.pets_adoption.dto.AdoptionLogsDTO;
import com.lurenjia.pets_adoption.dto.R;
import com.lurenjia.pets_adoption.entity.Adoptions;
import com.lurenjia.pets_adoption.entity.Pets;
import com.lurenjia.pets_adoption.entity.Users;
import com.lurenjia.pets_adoption.mapper.AdoptionsMapper;
import com.lurenjia.pets_adoption.service.IAdoptionsService;
import com.lurenjia.pets_adoption.service.IPetsService;
import com.lurenjia.pets_adoption.service.IUsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
public class AdoptionsServiceImpl extends ServiceImpl<AdoptionsMapper, Adoptions> implements IAdoptionsService {

    @Autowired
    @Lazy
    private IUsersService usersService;
    @Autowired
    @Lazy
    private IPetsService petsService;

    /**
     * 获取异常状态的领养人真实姓名
     * @return
     */
    @Override
    public R<List<String>> getErrUserName() {
        //获取到用户状态为异常的领养人
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Users::getUserStatus, 2);
        List<Users> usersList = usersService.list(queryWrapper);

        //判断 不存在
        if (usersList == null) {
            return R.error("不存在未按规定发布领养记录的领养人");
        }

        //获取真实姓名
        List<String> realNames = new ArrayList<>();
        for (Users users : usersList) {
            realNames.add(users.getUserRealname());
        }
        //如果存在，则返回异常领养人姓名
        if (!realNames.isEmpty()) {
            return R.success(realNames);
        }
        return R.error("不存在未按规定发布领养记录的领养人");
    }

    /**
     * 领养人取消领养
     * @param adoId
     * @param session
     * @return
     */
    @Transactional
    @Override
    public R<String> cancelAdoption(Long adoId, HttpSession session) {
        //领养记录
        Adoptions adoption = this.getById(adoId);
        //领养人
        Users user = usersService.getById(adoption.getUserId());
        //被领宠物
        Pets pet = petsService.getById(adoption.getPetId());

        //修改操作
        adoption.setAdoStatus(0);
        adoption.setAdoNote("用户取消领养");

        user.setUserStatus(0);

        pet.setPetStatus(0);

        //持久化
        this.updateById(adoption);
        petsService.updateById(pet);
        usersService.updateById(user);

        return R.success("取消成功");
    }

    /**
     * 修改领养状态：领养成功、失败
     * @param adoId
     * @param flag
     * @param session
     * @return
     */
    @Transactional
    @Override
    public R<String> updateAdoptionStatus(Long adoId, Integer flag, HttpSession session) {
        //获取领养记录
        Adoptions adoptions = this.getById(adoId);
        //修改记录表
        adoptions.setAdoStatus(flag);
        if (flag == 1) {
            //领养成功
            adoptions.setAdoNote("领养成功！");
        } else {
            //领养失败
            adoptions.setAdoNote("领养失败！");
        }
        this.updateById(adoptions);

        //领养人
        Users user = usersService.getById(adoptions.getUserId());
        //宠物
        Pets pet = petsService.getById(adoptions.getPetId());

        if (flag == 1) {
            //领养成功
            pet.setPetStatus(1);
            if (user.getUserType() != 1) {
                //不是管理员，则设置为领养人
                user.setUserType(2);
            }
        } else {
            //领养失败
            pet.setPetStatus(0);
        }

        //领养人状态恢复正常
        user.setUserStatus(0);
        //更新数据
        usersService.updateById(user);
        petsService.updateById(pet);

        return R.success("操作成功");
    }

    /**
     * 获取领养记录列表，普通用户只能获取到自己的
     *
     * @param page
     * @param pageSize
     * @param petName
     * @param userRealname
     * @param adoptionStatus
     * @param session
     * @return
     */
    @Override
    public R<Page> getList(Integer page, Integer pageSize, String petName, String userRealname, Integer adoptionStatus, HttpSession session) {
        //通过宠物昵称，拿到符合条件的宠物id
        Long[] petIds = getPetIds(petName);
        //通过领养人姓名，拿到符合条件的领养人id
        Long[] userIds = getUserIds(userRealname);

        //查询条件对象
        LambdaQueryWrapper<Adoptions> queryWrapper = getQueryWrapper(petIds, userIds,adoptionStatus, (Users) session.getAttribute("user"));

        //进行查询数据
        Page<Adoptions> adoptionsPage = new Page<>(page,pageSize);
        this.page(adoptionsPage,queryWrapper);
        //分页数据对象
        Page<AdoptionLogsDTO> pageInfo = new Page<>(page, pageSize);

        //不拷贝数据对象
        BeanUtils.copyProperties(adoptionsPage,pageInfo,"records");

        //获取到record数据
        List<Adoptions> adoptions = adoptionsPage.getRecords();

        log.info("看看拿到的领养记录：{}", adoptions);

        //数据装配
        List<AdoptionLogsDTO> adoptionLogsDTOS = new ArrayList<>();

        //设置私有属性：具体的宠物信息，领养人信息
        for (int i = 0; i < adoptions.size(); i++) {
            AdoptionLogsDTO adoptionLogsDTO = new AdoptionLogsDTO();
            //复制信息
            BeanUtils.copyProperties(adoptions.get(i), adoptionLogsDTO);

            //获取宠物数据
            Pets pets = petsService.getById(adoptions.get(i).getPetId());
            //获取领养人数据
            Users users = usersService.getById(adoptions.get(i).getUserId());

            //装入DTO中
            adoptionLogsDTO.setPet(pets);
            adoptionLogsDTO.setUser(users);

            adoptionLogsDTOS.add(adoptionLogsDTO);
        }
        log.info("看看在最后的领养记录传输对象：{}", adoptionLogsDTOS);
        pageInfo.setRecords(adoptionLogsDTOS);

        //进行响应数据
        return R.success(pageInfo);
    }

    /**
     * 构建查询条件对象
     *
     * @param petIds
     * @param userIds
     * @param adoptionStatus
     * @param user
     * @return
     */
    private LambdaQueryWrapper<Adoptions> getQueryWrapper(Long[] petIds, Long[] userIds, Integer adoptionStatus, Users user) {
        LambdaQueryWrapper<Adoptions> queryWrapper = new LambdaQueryWrapper<>();
        //查询条件
        queryWrapper.in(petIds != null && petIds.length > 0, Adoptions::getPetId, petIds);
        queryWrapper.eq(adoptionStatus!=null,Adoptions::getAdoStatus,adoptionStatus);
        //排序条件
        queryWrapper.orderByDesc(Adoptions::getAdoDate);

        //操作者是普通用户，只能查看自己的领养记录
        if (user.getUserType() == 0 || user.getUserType()==2) {
            queryWrapper.eq(Adoptions::getUserId, user.getUserId());
        }else {
            //是管理员
            queryWrapper.in(userIds != null && userIds.length > 0, Adoptions::getUserId, userIds);
        }

        return queryWrapper;
    }

    /**
     * 通过领养人姓名，拿到符合条件的领养人id
     *
     * @param userRealname
     * @return
     */
    private Long[] getUserIds(String userRealname) {
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like((userRealname != null) && !(userRealname.isEmpty()), Users::getUserRealname, userRealname);


        List<Users> users = usersService.list(queryWrapper);

        Long[] ids = new Long[users.size()];

        for (int i = 0; i < users.size(); i++) {
            ids[i] = users.get(i).getUserId();
        }

        log.info("拿到符合条件的领养人id了：{}", Arrays.toString(ids));

        return ids;
    }

    /**
     * 通过宠物昵称，拿到符合条件的宠物id
     *
     * @param petName
     * @return
     */
    private Long[] getPetIds(String petName) {
        LambdaQueryWrapper<Pets> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(petName != null && !(petName.isEmpty()), Pets::getPetName, petName);
        List<Pets> pets = petsService.list(queryWrapper);

        Long[] ids = new Long[pets.size()];

        for (int i = 0; i < pets.size(); i++) {
            ids[i] = pets.get(i).getPetId();
        }

        log.info("拿到符合条件的宠物id了：{}", Arrays.toString(ids));

        return ids;
    }
}
