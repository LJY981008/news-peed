package com.example.newspeed.repository.follow;

import java.util.List;

/**
 * <p>follow table QueryDSL</p>
 *
 * @author 김도연
 */
public interface FollowRepositoryCustom {
    //팔로워 검색(리스트 반환)
    List<Long> findFollowedUserIdsByFollowingUserId(Long currentUserId);
}
