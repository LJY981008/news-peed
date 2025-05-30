package com.example.newspeed.dto.comment;

import com.example.newspeed.entity.Comment;
import lombok.Getter;

/**
 * <p>댓글 삭제 응답</p>
 *
 * @author 이준영
 */
@Getter
public class CommentDeleteResponseDto {

    private final Long commentId;
    private final Long postId;
    private final String userName;
    private final String content;

    public CommentDeleteResponseDto(Comment comment) {
        this.commentId = comment.getCommentId();
        this.content = comment.getContent();
        this.postId = comment.getPost().getPostId();
        this.userName = comment.getUser().getUserName();
    }
}
