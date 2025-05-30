package com.example.newspeed.repository;

import com.example.newspeed.entity.PostLike;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class LikeRepositoryCustomImpl implements LikeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public LikeRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

}
