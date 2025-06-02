package com.example.newspeed.repository.like;

import com.example.newspeed.entity.Post;
import com.example.newspeed.entity.PostLike;
import com.example.newspeed.entity.QPost;
import com.example.newspeed.entity.QPostLike;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * <p>좋아요 테이블 QueryDSL 구현체</p>
 *
 * @author 이준영
 */
@Repository
public class LikeRepositoryCustomImpl implements LikeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public LikeRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * <p>좋아요를 누른 포스트와 사용자의 이력 조회</p>
     *
     * @param postId 좋아요를 누른 포스트
     * @param userId 좋아요를 누른 사용자
     * @return 해당하는 좋아요 테이블
     */
    @Override
    public Optional<PostLike> findByPostIdAndUserId(Long postId, Long userId) {
        QPostLike postLike = QPostLike.postLike;

        return Optional.ofNullable(queryFactory
                .selectFrom(postLike)
                .where(postLike.post.postId.eq(postId), postLike.user.userId.eq(userId))
                .fetchOne()
        );
    }

    /**
     * <p>좋아요를 눌렀을 때 좋아요 개수 갱신</p>
     *
     * @param post   좋아요를 누른 포스트
     * @param status 좋아요 개수 증감 트리거
     */
    @Override
    public void updateLikeStatus(Post post, Boolean status) {
        QPost qPost = QPost.post;
        JPAUpdateClause updateClause = queryFactory.update(qPost).where(qPost.postId.eq(post.getPostId()));

        if (status) {
            updateClause.set(qPost.userLikeCount, qPost.userLikeCount.add(1));
        } else {
            updateClause.set(qPost.userLikeCount, qPost.userLikeCount.subtract(1));
        }
        updateClause.execute();
    }
}