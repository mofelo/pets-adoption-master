package com.lurenjia.pets_adoption.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lurenjia.pets_adoption.dto.CommunityCommentDTO;
import com.lurenjia.pets_adoption.dto.R;
import com.lurenjia.pets_adoption.entity.CommunityComment;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * <p>
 * 社区评论 服务接口
 * </p>
 *
 * @author lurenjia
 * @since 2023-04-01
 */
public interface ICommunityCommentService extends IService<CommunityComment> {

    /**
     * 添加评论
     * @param comment 评论信息
     * @param session 会话信息
     * @return 操作结果
     */
    R<String> addComment(CommunityComment comment, HttpSession session);

    /**
     * 获取帖子的评论列表
     * @param postId 帖子ID
     * @param session 会话信息
     * @return 评论列表
     */
    R<List<CommunityCommentDTO>> getCommentsByPostId(Long postId, HttpSession session);

    /**
     * 删除评论
     * @param commentId 评论ID
     * @param session 会话信息
     * @return 操作结果
     */
    R<String> deleteComment(Long commentId, HttpSession session);
}