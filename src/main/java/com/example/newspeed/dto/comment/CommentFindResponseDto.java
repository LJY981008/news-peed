package com.example.newspeed.dto.comment;

import com.example.newspeed.entity.Comment;
import lombok.Getter;

/**
 * <p>댓글 조회 응답</p>
 *
 * @author 이준영
 */
@Getter
public class CommentFindResponseDto extends BaseCommentResponseDto {
    private final String createdAt;
    private final String modifiedAt;

    public CommentFindResponseDto(Comment comment) {
        super(comment);
        this.createdAt = comment.getCreatedAt().toString();
        this.modifiedAt = comment.getModifiedAt().toString();
    }
}
