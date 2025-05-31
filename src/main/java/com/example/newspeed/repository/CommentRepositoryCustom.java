package com.example.newspeed.repository;

import com.example.newspeed.entity.Comment;

import java.util.List;

public interface CommentRepositoryCustom {
    List<Comment> findByPostIdAndDeletedFalse(Long postId);
}
