package com.lurenjia.pets_adoption.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lurenjia.pets_adoption.dto.CommunityCommentDTO;
import com.lurenjia.pets_adoption.dto.CommunityPostDTO;
import com.lurenjia.pets_adoption.dto.R;
import com.lurenjia.pets_adoption.entity.CommunityComment;
import com.lurenjia.pets_adoption.entity.CommunityPost;
import com.lurenjia.pets_adoption.service.ICommunityCommentService;
import com.lurenjia.pets_adoption.service.ICommunityLikeService;
import com.lurenjia.pets_adoption.service.ICommunityPostService;

import lombok.extern.slf4j.Slf4j;

/**
 * 社区交流控制器
 * 处理社区帖子、评论、点赞等请求
 *
 * @author lurenjia
 * @since 2023-04-01
 */
@Slf4j
@RestController
@RequestMapping("/community")
public class CommunityController {

    @Autowired
    private ICommunityPostService postService;

    @Autowired
    private ICommunityCommentService commentService;

    @Autowired
    private ICommunityLikeService likeService;

    @RequestMapping(value = "/post", method = RequestMethod.POST)
    public R<String> publishPost(@RequestBody CommunityPost post, HttpSession session) {
        return postService.publishPost(post, session);
    }

    @RequestMapping(value = "/posts", method = { RequestMethod.GET, RequestMethod.POST })
    public R<Page<CommunityPostDTO>> getPostList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) Integer postType,
            HttpSession session) {
        return postService.getPostList(page, pageSize, userName, postType, session);
    }

    @RequestMapping(value = "/post/{postId}", method = RequestMethod.GET)
    public R<CommunityPostDTO> getPostDetail(@PathVariable Long postId, HttpSession session) {
        return postService.getPostDetail(postId, session);
    }

    @RequestMapping(value = "/post", method = RequestMethod.PUT)
    public R<String> updatePost(@RequestBody CommunityPost post, HttpSession session) {
        return postService.updatePost(post, session);
    }

    @RequestMapping(value = "/post/{postId}", method = RequestMethod.DELETE)
    public R<String> deletePost(@PathVariable Long postId, HttpSession session) {
        return postService.deletePost(postId, session);
    }

    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public R<String> addComment(@RequestBody CommunityComment comment, HttpSession session) {
        return commentService.addComment(comment, session);
    }

    @RequestMapping(value = "/comments/{postId}", method = RequestMethod.GET)
    public R<List<CommunityCommentDTO>> getCommentsByPostId(@PathVariable Long postId, HttpSession session) {
        return commentService.getCommentsByPostId(postId, session);
    }

    @RequestMapping(value = "/like/{postId}", method = RequestMethod.POST)
    public R<Boolean> likePost(@PathVariable Long postId, HttpSession session) {
        return likeService.toggleLike(postId, session);
    }

    @RequestMapping(value = "/like/{postId}/status", method = RequestMethod.GET)
    public R<Boolean> isPostLiked(@PathVariable Long postId, HttpSession session) {
        return likeService.checkLikeStatus(postId, session);
    }

    @RequestMapping(value = "/comment/{commentId}", method = RequestMethod.DELETE)
    public R<String> deleteComment(@PathVariable Long commentId, HttpSession session) {
        return commentService.deleteComment(commentId, session);
    }
}