package com.example.newspeed.repository.like;

import com.example.newspeed.entity.Post;
import com.example.newspeed.entity.PostLike;

import java.util.Optional;

/**
 * <p>좋아요 테이블 QueryDSL interface</p>
 *
 * @author 이준영
 */
public interface LikeRepositoryCustom {
    Optional<PostLike> findByPostIdAndUserId(Long postId, Long userId);
    void updateLikeStatus(Post post, Boolean status);
}
