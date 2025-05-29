package com.example.newspeed.repository;

import com.example.newspeed.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findAllByFollowingUserId(Long followingUserId);

    List<Follow> findAllByFollowId(Long followedId);
    Long countByFollowingUserId(Long followingUserId);
    Long countByFollowId(Long followedId);
    boolean existsByFollowingUserIdAndFollowId(Long followingUserId, Long followedUserId);
    void deleteByFollowingUserIdAndFollowId(Long followingUserId, Long followedUserId);

}
