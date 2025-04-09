package com.lurenjia.pets_adoption.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lurenjia.pets_adoption.dto.R;
import com.lurenjia.pets_adoption.entity.Adoptions;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lurenjia
 * @since 2023-03-25
 */
public interface IAdoptionsService extends IService<Adoptions> {

    /**
     * 获取异常状态的领养人真实姓名
     * @return
     */
    R<List<String>> getErrUserName();

    /**
     * 领养人取消领养
     * @param adoId
     * @param session
     * @return
     */
    R<String> cancelAdoption(Long adoId, HttpSession session);

    /**
     * 修改领养状态：领养成功、失败
     * @param adoId
     * @param flag
     * @param session
     * @return
     */
    R<String> updateAdoptionStatus(Long adoId, Integer flag, HttpSession session);

    /**
     * 获取领养记录列表，普通用户只能获取到自己的
     * @param page
     * @param pageSize
     * @param petName
     * @param userRealname
     * @param adoptionStatus
     * @param session
     * @return
     */
    R<Page> getList(Integer page, Integer pageSize, String petName, String userRealname, Integer adoptionStatus, HttpSession session);
}
