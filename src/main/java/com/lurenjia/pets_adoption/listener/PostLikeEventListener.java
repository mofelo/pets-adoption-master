package com.lurenjia.pets_adoption.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.lurenjia.pets_adoption.event.PostLikedEvent;
import com.lurenjia.pets_adoption.event.PostUnlikedEvent;
import com.lurenjia.pets_adoption.service.ICommunityPostService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PostLikeEventListener {

    @Autowired
    private ICommunityPostService postService;

    @EventListener
    public void handlePostLikedEvent(PostLikedEvent event) {
        log.info("处理帖子点赞事件：postId={}, userId={}", event.getPost().getPostId(), event.getUserId());
        // 更新帖子点赞数
        event.getPost().setLikeCount(event.getPost().getLikeCount() + 1);
        postService.updateById(event.getPost());
    }

    @EventListener
    public void handlePostUnlikedEvent(PostUnlikedEvent event) {
        log.info("处理帖子取消点赞事件：postId={}, userId={}", event.getPost().getPostId(), event.getUserId());
        // 更新帖子点赞数
        event.getPost().setLikeCount(event.getPost().getLikeCount() - 1);
        postService.updateById(event.getPost());
    }
}