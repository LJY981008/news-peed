package com.example.newspeed.dto.comment;

import com.example.newspeed.entity.Comment;
import lombok.Getter;

/**
 * <p>댓글 삭제 응답</p>
 *
 * @author 이준영
 */
@Getter
public class CommentDeleteResponseDto extends BaseCommentResponseDto {
    public CommentDeleteResponseDto(Comment comment) {
        super(comment);
    }
}
