package com.example.newspeed.repository;

import com.example.newspeed.entity.PostLike;
import com.example.newspeed.entity.QPostLike;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.Optional;
import java.util.function.Predicate;

public class LikeRepositoryCustomImpl implements LikeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public LikeRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Optional<PostLike> findByPostIdAndUserId(Long postId, Long userId) {
        QPostLike postLike = QPostLike.postLike;

        return Optional.ofNullable(queryFactory
                .selectFrom(postLike)
                .where(postLike.post.postId.eq(postId), postLike.user.userId.eq(userId))
                .fetchOne()
        );
    }
}
