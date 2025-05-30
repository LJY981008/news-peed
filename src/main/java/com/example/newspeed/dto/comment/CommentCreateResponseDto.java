package com.example.newspeed.dto.comment;

import com.example.newspeed.entity.Comment;
import lombok.Getter;

/**
 * <p>댓글 생성 응답</p>
 *
 * @author 이준영
 */
@Getter
public class CommentCreateResponseDto extends BaseCommentResponseDto {
    public CommentCreateResponseDto(Comment comment) {
        super(comment);
    }
}
