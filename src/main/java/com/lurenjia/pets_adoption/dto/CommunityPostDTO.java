package com.lurenjia.pets_adoption.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 社区帖子数据传输对象
 * 包含帖子信息和发布者信息
 */
@Data
public class CommunityPostDTO {
    /**
     * 帖子ID
     */
    private Long postId;

    /**
     * 发布用户ID
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
     * 帖子标题
     */
    private String title;

    /**
     * 帖子内容
     */
    private String content;

    /**
     * 帖子图片
     */
    private String postImage;

    /**
     * 帖子类型：1饲养经验，2求助问答，3宠物展示
     */
    private Integer postType;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 浏览次数
     */
    private Integer viewCount;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 评论数
     */
    private Integer commentCount;
    
    /**
     * 当前用户是否点赞
     */
    private Boolean isLiked;
}