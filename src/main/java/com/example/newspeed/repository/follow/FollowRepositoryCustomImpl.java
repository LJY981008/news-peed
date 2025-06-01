package com.example.newspeed.repository.follow;

import com.example.newspeed.entity.QFollow;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>follow table QueryDSL 구현</p>
 *
 * @author 김도연
 */
@Repository
@RequiredArgsConstructor
public class FollowRepositoryCustomImpl implements FollowRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    @Override
    public List<Long> findFollowedUserIdsByFollowingUserId(Long currentUserId) {
        QFollow follow = QFollow.follow;

        return queryFactory
                .select(follow.followedUserId)
                .from(follow)
                .where(follow.followingUserId.eq(currentUserId))
                .fetch();
    }
}
