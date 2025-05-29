package com.example.newspeed.dto.comment;

import com.example.newspeed.entity.Comment;
import lombok.Getter;

@Getter
public class CommentRemoveResponseDto {
    private Long commentId;
    private Long postId;
    private String userName;
    private String content;

    public CommentRemoveResponseDto(Comment comment) {
        this.commentId = comment.getCommentId();
        this.content = comment.getContent();
        this.postId = comment.getPost().getPostId();
        this.userName = comment.getUser().getUserName();
    }
}
