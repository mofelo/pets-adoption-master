package com.lurenjia.pets_adoption.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lurenjia.pets_adoption.dto.CommunityPostDTO;
import com.lurenjia.pets_adoption.dto.R;
import com.lurenjia.pets_adoption.entity.CommunityPost;
import com.lurenjia.pets_adoption.entity.Users;
import com.lurenjia.pets_adoption.event.PostDeletedEvent;
import com.lurenjia.pets_adoption.event.PostPublishedEvent;
import com.lurenjia.pets_adoption.event.PostUpdatedEvent;
import com.lurenjia.pets_adoption.mapper.CommunityPostMapper;
import com.lurenjia.pets_adoption.service.ICommunityLikeService;
import com.lurenjia.pets_adoption.service.ICommunityPostService;
import com.lurenjia.pets_adoption.service.IUsersService;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 社区帖子 服务实现类
 * </p>
 *
 * @author lurenjia
 * @since 2023-04-01
 */
@Service
@Slf4j
public class CommunityPostServiceImpl extends ServiceImpl<CommunityPostMapper, CommunityPost>
        implements ICommunityPostService {

    @Autowired
    private IUsersService usersService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private ICommunityLikeService likeService;

    @Autowired
    public CommunityPostServiceImpl(IUsersService usersService, ApplicationEventPublisher eventPublisher,
            ICommunityLikeService likeService) {
        this.usersService = usersService;
        this.eventPublisher = eventPublisher;
        this.likeService = likeService;
    }

    // 移除likeService依赖，改用事件监听处理点赞相关逻辑

    @Override
    @Transactional
    public R<String> publishPost(CommunityPost post, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return R.error("用户未登录");
        }

        post.setUserId(userId);
        post.setCreateTime(LocalDateTime.now());
        post.setUpdateTime(LocalDateTime.now());
        post.setViewCount(0);
        post.setLikeCount(0);
        post.setCommentCount(0);
        post.setStatus(1);

        boolean success = this.save(post);
        if (success) {
            eventPublisher.publishEvent(new PostPublishedEvent(post));
            return R.success("发布成功");
        } else {
            return R.error("发布失败");
        }
    }

    @Override
    public R<String> updatePost(CommunityPost post, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return R.error("用户未登录");
        }

        CommunityPost originalPost = this.getById(post.getPostId());
        if (originalPost == null || originalPost.getStatus() == 0) {
            return R.error("帖子不存在或已删除");
        }

        Integer userType = (Integer) session.getAttribute("userType");
        if (!originalPost.getUserId().equals(userId) && (userType == null || userType != 1)) {
            return R.error("无权修改该帖子");
        }

        post.setUpdateTime(LocalDateTime.now());
        originalPost.setTitle(post.getTitle());
        originalPost.setContent(post.getContent());
        originalPost.setPostType(post.getPostType());
        originalPost.setPostImage(post.getPostImage());
        originalPost.setUpdateTime(post.getUpdateTime());

        boolean success = this.updateById(originalPost);
        if (success) {
            eventPublisher.publishEvent(new PostUpdatedEvent(originalPost));
            return R.success("更新成功");
        } else {
            return R.error("更新失败");
        }
    }

    @Override
    public R<String> deletePost(Long postId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return R.error("用户未登录");
        }

        CommunityPost post = this.getById(postId);
        if (post == null || post.getStatus() == 0) {
            return R.error("帖子不存在或已删除");
        }

        Integer userType = (Integer) session.getAttribute("userType");
        if (!post.getUserId().equals(userId) && (userType == null || userType != 1)) {
            return R.error("无权删除该帖子");
        }

        post.setStatus(0);
        boolean success = this.updateById(post);
        if (success) {
            eventPublisher.publishEvent(new PostDeletedEvent(post));
            return R.success("删除成功");
        } else {
            return R.error("删除失败");
        }
    }

    /**
     * 分页查询帖子列表
     *
     * @param page     页码
     * @param pageSize 每页大小
     * @param userName 用户名
     * @param postType 帖子类型
     * @param session  会话信息
     * @return 帖子列表
     */
    @Override
    public R<Page<CommunityPostDTO>> getPostList(Integer page, Integer pageSize, String userName, Integer postType,
            HttpSession session) {
        // 获取当前用户ID
        Long currentUserId = (Long) session.getAttribute("userId");

        // 构建查询条件
        Page<CommunityPost> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<CommunityPost> queryWrapper = new LambdaQueryWrapper<>();

        // 只查询状态正常的帖子
        queryWrapper.eq(CommunityPost::getStatus, 1);

        // 按帖子类型查询
        if (postType != null) {
            queryWrapper.eq(CommunityPost::getPostType, postType);
        }

        // 按用户名查询需要先获取用户ID
        if (userName != null && !userName.isEmpty()) {
            LambdaQueryWrapper<Users> userWrapper = new LambdaQueryWrapper<>();
            userWrapper.like(Users::getUserName, userName);
            List<Users> users = usersService.list(userWrapper);
            if (users != null && !users.isEmpty()) {
                List<Long> userIds = users.stream().map(Users::getUserId).collect(Collectors.toList());
                queryWrapper.in(CommunityPost::getUserId, userIds);
            } else {
                // 如果没有找到匹配的用户，返回空结果
                Page<CommunityPostDTO> emptyPage = new Page<>(page, pageSize);
                emptyPage.setRecords(new ArrayList<>());
                return R.success(emptyPage);
            }
        }

        // 按创建时间降序排序
        queryWrapper.orderByDesc(CommunityPost::getCreateTime);

        // 执行查询
        this.page(pageInfo, queryWrapper);

        // 转换为DTO对象
        Page<CommunityPostDTO> dtoPage = new Page<>(page, pageSize);
        dtoPage.setTotal(pageInfo.getTotal());

        List<CommunityPostDTO> dtoList = pageInfo.getRecords().stream().map(post -> {
            CommunityPostDTO dto = new CommunityPostDTO();
            BeanUtils.copyProperties(post, dto);

            // 获取用户信息
            Users user = usersService.getById(post.getUserId());
            if (user != null) {
                dto.setUserName(user.getUserName());
                dto.setUserRealname(user.getUserRealname());
            }

            // 检查当前用户是否点赞
            if (currentUserId != null) {
                try {
                    R<Boolean> likeStatus = likeService.checkLikeStatus(post.getPostId(), session);
                    dto.setIsLiked(likeStatus.getData());
                } catch (Exception e) {
                    log.error("获取点赞状态失败: {}", e.getMessage());
                    dto.setIsLiked(false);
                }
            } else {
                dto.setIsLiked(false);
            }

            return dto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(dtoList);

        return R.success(dtoPage);
    }

    /**
     * 获取帖子详情
     *
     * @param postId  帖子ID
     * @param session 会话信息
     * @return 帖子详情
     */
    @Override
    @Transactional
    public R<CommunityPostDTO> getPostDetail(Long postId, HttpSession session) {
        // 获取当前用户ID
        Long currentUserId = (Long) session.getAttribute("userId");

        // 查询帖子
        CommunityPost post = this.getById(postId);
        if (post == null || post.getStatus() == 0) {
            return R.error("帖子不存在或已删除");
        }

        // 增加浏览次数
        post.setViewCount(post.getViewCount() + 1);
        this.updateById(post);

        // 转换为DTO对象
        CommunityPostDTO dto = new CommunityPostDTO();
        BeanUtils.copyProperties(post, dto);

        // 获取用户信息
        Users user = usersService.getById(post.getUserId());
        if (user != null) {
            dto.setUserName(user.getUserName());
            dto.setUserRealname(user.getUserRealname());
        }

        // 检查当前用户是否点赞
        if (currentUserId != null) {
            try {
                R<Boolean> likeStatus = likeService.checkLikeStatus(post.getPostId(), session);
                dto.setIsLiked(likeStatus.getData());
            } catch (Exception e) {
                log.error("获取点赞状态失败: {}", e.getMessage());
                dto.setIsLiked(false);
            }
        } else {
            dto.setIsLiked(false);
        }

        return R.success(dto);
    }

    /**
     * 更新帖子
     *
     * @param post    帖子信息
     * @param session 会话信息
     * @return 操作结果
     */

}