package com.example.newspeed.repository.Comment;

import com.example.newspeed.entity.Comment;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepositoryCustom {
    List<Comment> findCommentByPostId(@Param("postId") Long postId);
    Optional<Comment> findCommentByCommentId(@Param("commentId") Long commentId);
}
