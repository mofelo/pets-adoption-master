package com.lurenjia.pets_adoption.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 社区评论数据传输对象
 * 包含评论信息和评论者信息
 */
@Data
public class CommunityCommentDTO {
    /**
     * 评论ID
     */
    private Long commentId;

    /**
     * 帖子ID
     */
    private Long postId;

    /**
     * 评论用户ID
     */
    private Long userId;
    
    /**
     * 用户昵称
     */
    private String userName;
    
    /**
     * 用户真实姓名
     */
    private String userRealname;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 父评论ID，0表示一级评论
     */
    private Long parentId;
    
    /**
     * 父评论用户名称，用于回复显示
     */
    private String parentUserName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 子评论列表
     */
    private List<CommunityCommentDTO> children;
}