package com.lurenjia.pets_adoption.service;

import javax.servlet.http.HttpSession;
//import org.springframework.web.bind.annotation.PostMapping;

import com.lurenjia.pets_adoption.dto.R;

public interface ILikeService {
    // 修改返回类型为 R<Boolean>
    R<Boolean> checkLikeStatus(Long postId, HttpSession session);

    /**
     * 点赞/取消点赞
     *
     * @param postId  帖子ID
     * @param session 会话信息
     * @return 操作结果
     */
    R<Boolean> toggleLike(Long postId, HttpSession session);
}