package com.example.newspeed.repository;

import com.example.newspeed.entity.Comment;
import com.example.newspeed.entity.QComment;
import com.example.newspeed.entity.QPost;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;

public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public CommentRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<Comment> findByPostIdAndDeletedFalse(Long postId) {
        QComment qComment = QComment.comment;

        return queryFactory.selectFrom(qComment)
                .where(qComment.post.postId.eq(postId), qComment.isDeleted.isFalse())
                .fetch();
    }
}
