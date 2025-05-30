package com.example.newspeed.repository;

import com.example.newspeed.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findAllByFollowingUserId(Long followingUserId);

    List<Follow> findAllByFollowedUserId(Long followedUserId);
    Long countByFollowingUserId(Long followingUserId);
    Long countByFollowedUserId(Long followedUserId);
    boolean existsByFollowingUserIdAndFollowedUserId(Long followingUserId, Long followedUserId);
    void deleteByFollowingUserIdAndFollowedUserId(Long followingUserId, Long followedUserId);

    @Query("SELECT f.followedUserId FROM Follow f WHERE f.followingUserId = :currentUserId")
    List<Long> findFollowedUserIdsByFollowingUserId(@Param("currentUserId") Long currentUserId);
}
