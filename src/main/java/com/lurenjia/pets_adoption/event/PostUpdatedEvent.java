package com.lurenjia.pets_adoption.event;

import org.springframework.context.ApplicationEvent;

import com.lurenjia.pets_adoption.entity.CommunityPost;

/**
 * 帖子更新事件
 */
public class PostUpdatedEvent extends ApplicationEvent {
    private final CommunityPost post;

    public PostUpdatedEvent(CommunityPost post) {
        super(post);
        this.post = post;
    }

    public CommunityPost getPost() {
        return post;
    }
}