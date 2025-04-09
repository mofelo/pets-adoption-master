package com.lurenjia.pets_adoption.event;

import org.springframework.context.ApplicationEvent;

import com.lurenjia.pets_adoption.entity.CommunityPost;

/**
 * 帖子取消点赞事件
 */
public class PostUnlikedEvent extends ApplicationEvent {
    private final CommunityPost post;
    private final Long userId;

    public PostUnlikedEvent(CommunityPost post, Long userId) {
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