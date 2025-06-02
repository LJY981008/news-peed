package com.example.newspeed.repository.Comment;

import com.example.newspeed.entity.Comment;
import com.example.newspeed.entity.QComment;
import com.example.newspeed.entity.QPost;
import com.example.newspeed.entity.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public CommentRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Query("SELECT c FROM Comment c " +
            "LEFT JOIN FETCH c.user " +
            "WHERE c.post.postId = :postId AND c.deleted = false")
    /**
     * 페치 조인 적용 포스트 기준 댓글 전체 조회
     *
     * @param postId
     * @return
     */
    @Override
    public List<Comment> findCommentByPostId(Long postId) {
        QComment comment = QComment.comment;
        QUser user = QUser.user;

        return queryFactory.selectFrom(comment)
                .leftJoin(comment.user, user)
                .fetchJoin()
                .where(
                        comment.post.postId.eq(postId),
                        comment.deleted.isFalse()
                )
                .orderBy(comment.createdAt.desc())
                .fetch();
    }
}
