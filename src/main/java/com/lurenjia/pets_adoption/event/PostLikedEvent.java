package com.lurenjia.pets_adoption.event;

import org.springframework.context.ApplicationEvent;

import com.lurenjia.pets_adoption.entity.CommunityPost;

/**
 * 帖子点赞事件
 */
public class PostLikedEvent extends ApplicationEvent {
    private final CommunityPost post;
    private final Long userId;

    public PostLikedEvent(CommunityPost post, Long userId) {
        super(post);
        this.post = post;
        this.userId = userId;
    }

    public CommunityPost getPost() {
        return post;
    }

    public Long getUserId() {
        return userId;
    }
}