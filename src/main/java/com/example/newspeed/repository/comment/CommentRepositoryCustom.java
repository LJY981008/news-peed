package com.example.newspeed.repository.comment;

import com.example.newspeed.entity.Comment;

import java.util.List;

public interface CommentRepositoryCustom {
    List<Comment> findCommentsByPostIdAndDeletedFalse(Long postId);
}
