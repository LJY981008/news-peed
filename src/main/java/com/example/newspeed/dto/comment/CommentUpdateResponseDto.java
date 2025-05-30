package com.example.newspeed.dto.comment;

import com.example.newspeed.entity.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class CommentUpdateResponseDto {
    private Long commentId;
    private Long postId;
    private String userName;
    private String content;
    private String createdAt;
    private String modifiedAt;

    @Setter
    private String prevContent;

    public CommentUpdateResponseDto(Comment comment) {
        this.commentId = comment.getCommentId();
        this.postId = comment.getPost().getPostId();
        this.userName = comment.getUser().getUserName();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt().toString();
        this.modifiedAt = comment.getModifiedAt().toString();
    }
}
