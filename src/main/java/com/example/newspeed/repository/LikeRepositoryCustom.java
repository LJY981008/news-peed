package com.example.newspeed.repository;

import com.example.newspeed.entity.PostLike;

import java.util.Optional;

public interface LikeRepositoryCustom {
    Optional<PostLike> findByPostIdAndUserId(Long postId, Long userId);
}
