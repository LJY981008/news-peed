package com.example.newspeed.dto.comment;

import com.example.newspeed.entity.Comment;
import lombok.Getter;

@Getter
public class CommentFindResponseDto {
    private Long commentId;
    private Long postId;
    private String userName;
    private String content;
    //private int likeCount;
    private String createdAt;
    private String modifiedAt;

    public CommentFindResponseDto(Comment comment) {
        this.commentId = comment.getCommentId();
        this.postId = comment.getPost().getPostId();
        this.userName = comment.getUser().getUserName();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt().toString();
        this.modifiedAt = comment.getModifiedAt().toString();
    }
}
