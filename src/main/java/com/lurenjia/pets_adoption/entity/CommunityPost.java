package com.lurenjia.pets_adoption.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 社区帖子实体类
 * </p>
 *
 * @author lurenjia
 * @since 2023-04-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CommunityPost implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 帖子ID
     */
    @TableId(value = "post_id", type = IdType.AUTO)
    private Long postId;

    /**
     * 发布用户ID
     */
    private Long userId;

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
     * 状态：0删除，1正常
     */
    private Integer status;
}