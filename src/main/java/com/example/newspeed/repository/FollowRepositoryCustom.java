package com.example.newspeed.repository;

import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FollowRepositoryCustom {
    List<Long> findFollowedUserIdsByFollowingUserId(Long currentUserId);
}
