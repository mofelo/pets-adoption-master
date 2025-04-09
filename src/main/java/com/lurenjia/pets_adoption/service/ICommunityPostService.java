package com.lurenjia.pets_adoption.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lurenjia.pets_adoption.dto.CommunityPostDTO;
import com.lurenjia.pets_adoption.dto.R;
import com.lurenjia.pets_adoption.entity.CommunityPost;

import javax.servlet.http.HttpSession;

/**
 * <p>
 * 社区帖子 服务接口
 * </p>
 *
 * @author lurenjia
 * @since 2023-04-01
 */
public interface ICommunityPostService extends IService<CommunityPost> {

    /**
     * 发布帖子
     * @param post 帖子信息
     * @param session 会话信息
     * @return 操作结果
     */
    R<String> publishPost(CommunityPost post, HttpSession session);

    /**
     * 分页查询帖子列表
     * @param page 页码
     * @param pageSize 每页大小
     * @param userName 用户名
     * @param postType 帖子类型
     * @param session 会话信息
     * @return 帖子列表
     */
    R<Page<CommunityPostDTO>> getPostList(Integer page, Integer pageSize, String userName, Integer postType, HttpSession session);

    /**
     * 获取帖子详情
     * @param postId 帖子ID
     * @param session 会话信息
     * @return 帖子详情
     */
    R<CommunityPostDTO> getPostDetail(Long postId, HttpSession session);

    /**
     * 更新帖子
     * @param post 帖子信息
     * @param session 会话信息
     * @return 操作结果
     */
    R<String> updatePost(CommunityPost post, HttpSession session);

    /**
     * 删除帖子
     * @param postId 帖子ID
     * @param session 会话信息
     * @return 操作结果
     */
    R<String> deletePost(Long postId, HttpSession session);
}