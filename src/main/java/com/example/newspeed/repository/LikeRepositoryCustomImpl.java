package com.example.newspeed.repository;

import com.example.newspeed.entity.Post;
import com.example.newspeed.entity.PostLike;
import com.example.newspeed.entity.QPost;
import com.example.newspeed.entity.QPostLike;
import com.querydsl.core.types.Path;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;

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

    @Override
    public void updateLikeStatus(Post post, Boolean status) {
        QPost qPost = QPost.post;
        JPAUpdateClause updateClause = queryFactory.update(qPost).where(qPost.postId.eq(post.getPostId()));

        if(status) {
            updateClause.set(qPost.userLikeCount, qPost.userLikeCount.add(1));
        } else {
            updateClause.set(qPost.userLikeCount, qPost.userLikeCount.subtract(1));
        }
        updateClause.execute();
    }
}
