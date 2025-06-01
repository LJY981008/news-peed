package com.example.newspeed.repository.follow;

import com.example.newspeed.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>follow table 레포지토리</p>
 *
 * @author 김도연
 */
@Repository
public interface FollowRepository extends JpaRepository<Follow, Long>, FollowRepositoryCustom{
    List<Follow> findAllByFollowingUserId(Long followingUserId);

    List<Follow> findAllByFollowedUserId(Long followedUserId);
    Long countByFollowingUserId(Long followingUserId);
    Long countByFollowedUserId(Long followedUserId);

    //팔로우 관계 존재 여부
    boolean existsByFollowingUserIdAndFollowedUserId(Long followingUserId, Long followedUserId);

    //팔로우 관계 삭제(언팔로우)
    void deleteByFollowingUserIdAndFollowedUserId(Long followingUserId, Long followedUserId);
}
