package com.example.newspeed.dto.comment;

import com.example.newspeed.entity.Comment;
import lombok.Getter;

@Getter
public abstract class BaseCommentResponseDto {

    private final Long commentId;
    private final Long postId;
    private final String userName;
    private final String content;

    protected BaseCommentResponseDto(Comment comment) {
        this.commentId = comment.getCommentId();
        this.postId = comment.getPost().getPostId();
        this.userName = comment.getUser().getUserName();
        this.content = comment.getContent();
    }
}
