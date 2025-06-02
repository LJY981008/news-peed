package com.example.newspeed.repository.Comment;

import com.example.newspeed.entity.Comment;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepositoryCustom {
    List<Comment> findCommentByPostId(@Param("postId") Long postId);
}
