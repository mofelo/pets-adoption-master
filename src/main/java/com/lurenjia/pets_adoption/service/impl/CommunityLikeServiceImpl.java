package com.lurenjia.pets_adoption.service.impl;

import java.time.LocalDateTime;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lurenjia.pets_adoption.dto.R;
import com.lurenjia.pets_adoption.entity.CommunityLike;
import com.lurenjia.pets_adoption.entity.CommunityPost;
import com.lurenjia.pets_adoption.event.PostLikedEvent;
import com.lurenjia.pets_adoption.event.PostUnlikedEvent;
import com.lurenjia.pets_adoption.mapper.CommunityLikeMapper;
import com.lurenjia.pets_adoption.service.ICommunityLikeService;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 社区点赞 服务实现类
 * </p>
 *
 * @author lurenjia
 * @since 2023-04-01
 */
@Service
@Slf4j
public class CommunityLikeServiceImpl extends ServiceImpl<CommunityLikeMapper, CommunityLike>
        implements ICommunityLikeService {

    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public CommunityLikeServiceImpl(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    /**
     * 点赞/取消点赞
     *
     * @param postId  帖子ID
     * @param session 会话信息
     * @return 操作结果
     */
    @Override
    @Transactional
    public R<Boolean> toggleLike(Long postId, HttpSession session) {
        // 获取当前用户ID
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return R.error("用户未登录");
        }

        // 创建点赞事件对象，让监听器处理帖子状态检查
        CommunityPost post = new CommunityPost();
        post.setPostId(postId);

        try {
            // 检查是否已点赞
            LambdaQueryWrapper<CommunityLike> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(CommunityLike::getPostId, postId);
            queryWrapper.eq(CommunityLike::getUserId, userId);
            CommunityLike like = this.getOne(queryWrapper);

            boolean result;
            if (like == null) {
                // 未点赞，添加点赞记录
                like = new CommunityLike();
                like.setPostId(postId);
                like.setUserId(userId);
                like.setCreateTime(LocalDateTime.now());
                result = this.save(like);

                // 发布点赞事件
                if (result) {
                    try {
                        eventPublisher.publishEvent(new PostLikedEvent(post, userId));
                    } catch (Exception e) {
                        log.error("发布点赞事件失败: {}", e.getMessage());
                    }
                }

                return R.success(true);
            } else {
                // 已点赞，取消点赞
                result = this.remove(queryWrapper);

                // 发布取消点赞事件
                if (result) {
                    try {
                        eventPublisher.publishEvent(new PostUnlikedEvent(post, userId));
                    } catch (Exception e) {
                        log.error("发布取消点赞事件失败: {}", e.getMessage());
                    }
                }

                return R.success(false);
            }
        } catch (Exception e) {
            log.error("处理点赞操作失败: {}", e.getMessage());
            return R.error("点赞操作失败，请稍后再试");
        }
    }

    @Override
    public R<Boolean> checkLikeStatus(Long postId, HttpSession session) {
        // 获取当前用户ID
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return R.success(false);
        }

        try {
            // 检查是否已点赞
            LambdaQueryWrapper<CommunityLike> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(CommunityLike::getPostId, postId);
            queryWrapper.eq(CommunityLike::getUserId, userId);
            CommunityLike like = this.getOne(queryWrapper);

            return R.success(like != null);
        } catch (Exception e) {
            log.error("检查点赞状态失败: {}", e.getMessage());
            return R.success(false); // 出错时默认为未点赞
        }
    }
}