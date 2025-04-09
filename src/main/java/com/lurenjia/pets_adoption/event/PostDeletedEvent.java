package com.lurenjia.pets_adoption.event;

import org.springframework.context.ApplicationEvent;

import com.lurenjia.pets_adoption.entity.CommunityPost;

/**
 * 帖子删除事件
 */
public class PostDeletedEvent extends ApplicationEvent {
    private final CommunityPost post;

    public PostDeletedEvent(CommunityPost post) {
        super(post);
        this.post = post;
    }

    public CommunityPost getPost() {
        return post;
    }
}