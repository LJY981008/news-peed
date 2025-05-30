package com.example.newspeed.dto.comment;

import com.example.newspeed.entity.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>댓글 수정 응답</p>
 *
 * @author 이준영
 */
@Getter
public class CommentUpdateResponseDto {

    private final Long commentId;
    private final Long postId;
    private final String userName;
    private final String content;
    private final String createdAt;
    private final String modifiedAt;

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
