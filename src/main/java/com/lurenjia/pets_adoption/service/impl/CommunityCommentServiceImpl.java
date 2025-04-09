package com.lurenjia.pets_adoption.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lurenjia.pets_adoption.dto.CommunityCommentDTO;
import com.lurenjia.pets_adoption.dto.R;
import com.lurenjia.pets_adoption.entity.CommunityComment;
import com.lurenjia.pets_adoption.entity.CommunityPost;
import com.lurenjia.pets_adoption.entity.Users;
import com.lurenjia.pets_adoption.mapper.CommunityCommentMapper;
import com.lurenjia.pets_adoption.service.ICommunityCommentService;
import com.lurenjia.pets_adoption.service.ICommunityPostService;
import com.lurenjia.pets_adoption.service.IUsersService;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 社区评论 服务实现类
 * </p>
 *
 * @author lurenjia
 * @since 2023-04-01
 */
@Service
@Slf4j
public class CommunityCommentServiceImpl extends ServiceImpl<CommunityCommentMapper, CommunityComment>
        implements ICommunityCommentService {

    @Autowired
    private IUsersService usersService;

    @Autowired
    private ICommunityPostService postService;

    /**
     * 添加评论
     *
     * @param comment 评论信息
     * @param session 会话信息
     * @return 操作结果
     */
    @Override
    @Transactional
    public R<String> addComment(CommunityComment comment, HttpSession session) {
        // 获取当前用户ID
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return R.error("用户未登录");
        }

        // 检查帖子是否存在
        CommunityPost post = postService.getById(comment.getPostId());
        if (post == null || post.getStatus() == 0) {
            return R.error("帖子不存在或已删除");
        }

        // 设置评论信息
        comment.setUserId(userId);
        comment.setCreateTime(LocalDateTime.now());
        comment.setStatus(1); // 正常状态

        // 如果是回复评论，检查父评论是否存在
        if (comment.getParentId() != null && comment.getParentId() > 0) {
            CommunityComment parentComment = this.getById(comment.getParentId());
            if (parentComment == null || parentComment.getStatus() == 0) {
                return R.error("回复的评论不存在或已删除");
            }
        } else {
            // 一级评论，父ID设为0
            comment.setParentId(0L);
        }

        // 保存评论
        boolean success = this.save(comment);
        if (success) {
            // 更新帖子评论数
            post.setCommentCount(post.getCommentCount() + 1);
            postService.updateById(post);
            return R.success("评论成功");
        } else {
            return R.error("评论失败");
        }
    }

    /**
     * 获取帖子的评论列表
     *
     * @param postId  帖子ID
     * @param session 会话信息
     * @return 评论列表
     */
    @Override
    public R<List<CommunityCommentDTO>> getCommentsByPostId(Long postId, HttpSession session) {
        // 检查帖子是否存在
        CommunityPost post = postService.getById(postId);
        if (post == null || post.getStatus() == 0) {
            return R.error("帖子不存在或已删除");
        }

        // 查询所有评论
        LambdaQueryWrapper<CommunityComment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CommunityComment::getPostId, postId);
        queryWrapper.eq(CommunityComment::getStatus, 1); // 只查询正常状态的评论
        queryWrapper.orderByAsc(CommunityComment::getCreateTime); // 按时间升序排序
        List<CommunityComment> comments = this.list(queryWrapper);

        // 获取所有评论用户的信息
        List<Long> userIds = comments.stream().map(CommunityComment::getUserId).distinct().collect(Collectors.toList());
        Map<Long, Users> userMap = new HashMap<>();
        if (!userIds.isEmpty()) {
            List<Users> users = usersService.listByIds(userIds);
            userMap = users.stream().collect(Collectors.toMap(Users::getUserId, user -> user));
        }

        // 转换为DTO对象并构建评论树
        Map<Long, Users> finalUserMap = userMap;
        Map<Long, CommunityCommentDTO> commentDTOMap = new HashMap<>();

        // 先转换所有评论为DTO
        for (CommunityComment comment : comments) {
            CommunityCommentDTO dto = new CommunityCommentDTO();
            BeanUtils.copyProperties(comment, dto);

            // 设置用户信息
            Users user = finalUserMap.get(comment.getUserId());
            if (user != null) {
                dto.setUserName(user.getUserName());
                dto.setUserRealname(user.getUserRealname());
            }

            // 初始化子评论列表
            dto.setChildren(new ArrayList<>());

            commentDTOMap.put(dto.getCommentId(), dto);
        }

        // 构建评论树
        List<CommunityCommentDTO> rootComments = new ArrayList<>();
        for (CommunityCommentDTO dto : commentDTOMap.values()) {
            // 如果是一级评论
            if (dto.getParentId() == 0) {
                rootComments.add(dto);
            } else {
                // 如果是回复评论，添加到父评论的子评论列表中
                CommunityCommentDTO parentDTO = commentDTOMap.get(dto.getParentId());
                if (parentDTO != null) {
                    // 设置父评论用户名
                    dto.setParentUserName(parentDTO.getUserName());
                    parentDTO.getChildren().add(dto);
                } else {
                    // 如果找不到父评论，作为一级评论处理
                    rootComments.add(dto);
                }
            }
        }

        return R.success(rootComments);
    }

    /**
     * 删除评论
     *
     * @param commentId 评论ID
     * @param session   会话信息
     * @return 操作结果
     */
    @Override
    @Transactional
    public R<String> deleteComment(Long commentId, HttpSession session) {
        // 获取当前用户ID
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return R.error("用户未登录");
        }

        // 查询评论
        CommunityComment comment = this.getById(commentId);
        if (comment == null || comment.getStatus() == 0) {
            return R.error("评论不存在或已删除");
        }

        // 检查是否是评论作者或管理员
        Integer userType = (Integer) session.getAttribute("userType");
        if (!comment.getUserId().equals(userId) && (userType == null || userType != 1)) {
            return R.error("无权删除该评论");
        }

        // 逻辑删除评论
        comment.setStatus(0);
        boolean success = this.updateById(comment);

        if (success) {
            // 更新帖子评论数
            CommunityPost post = postService.getById(comment.getPostId());
            if (post != null) {
                post.setCommentCount(post.getCommentCount() - 1);
                postService.updateById(post);
            }

            // 递归删除子评论
            LambdaQueryWrapper<CommunityComment> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(CommunityComment::getParentId, commentId);
            queryWrapper.eq(CommunityComment::getStatus, 1);
            List<CommunityComment> childComments = this.list(queryWrapper);

            for (CommunityComment childComment : childComments) {
                this.deleteComment(childComment.getCommentId(), session);
            }

            return R.success("删除成功");
        } else {
            return R.error("删除失败");
        }
    }
}