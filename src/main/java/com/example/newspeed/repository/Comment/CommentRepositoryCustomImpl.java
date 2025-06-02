package com.example.newspeed.repository.Comment;

import com.example.newspeed.entity.Comment;
import com.example.newspeed.entity.QComment;
import com.example.newspeed.entity.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;
import java.util.Optional;

public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public CommentRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * 페치 조인 적용 포스트 기준 댓글 전체 조회
     *
     * @param postId 조회할 포스트 ID
     * @return 페치조인한 댓글 리스트
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

    /**
     * 페치 조인 적용 댓글 단건 조회
     *
     * @param commentId 조회할 댓글 ID
     * @return 페치조인한 댓글
     */
    @Override
    public Optional<Comment> findCommentByCommentId(Long commentId) {
        QComment comment = QComment.comment;
        QUser user = QUser.user;

        return queryFactory.selectFrom(comment)
                .leftJoin(comment.user, user)
                .fetchJoin()
                .where(
                        comment.commentId.eq(commentId),
                        comment.deleted.isFalse()
                )
                .stream().findAny();
    }
}
