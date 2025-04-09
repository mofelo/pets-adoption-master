package com.lurenjia.pets_adoption.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lurenjia.pets_adoption.dto.R;
import com.lurenjia.pets_adoption.entity.CommunityLike;

import javax.servlet.http.HttpSession;

/**
 * <p>
 * 社区点赞 服务接口
 * </p>
 *
 * @author lurenjia
 * @since 2023-04-01
 */
public interface ICommunityLikeService extends IService<CommunityLike> {

    /**
     * 点赞/取消点赞
     * @param postId 帖子ID
     * @param session 会话信息
     * @return 操作结果
     */
    R<Boolean> toggleLike(Long postId, HttpSession session);

    /**
     * 检查用户是否点赞
     * @param postId 帖子ID
     * @param session 会话信息
     * @return 是否点赞
     */
    R<Boolean> checkLikeStatus(Long postId, HttpSession session);
}