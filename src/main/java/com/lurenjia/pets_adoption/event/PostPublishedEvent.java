package com.lurenjia.pets_adoption.event;

import org.springframework.context.ApplicationEvent;

import com.lurenjia.pets_adoption.entity.CommunityPost;

/**
 * 帖子发布事件
 */
public class PostPublishedEvent extends ApplicationEvent {
    private final CommunityPost post;

    public PostPublishedEvent(CommunityPost post) {
        super(post);
        this.post = post;
    }

    public CommunityPost getPost() {
        return post;
    }
}