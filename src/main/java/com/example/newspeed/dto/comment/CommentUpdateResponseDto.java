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
public class CommentUpdateResponseDto extends BaseCommentResponseDto {

    private final String createdAt;
    private final String modifiedAt;

    @Setter
    private String prevContent;

    public CommentUpdateResponseDto(Comment comment) {
        super(comment);
        this.createdAt = comment.getCreatedAt().toString();
        this.modifiedAt = comment.getModifiedAt().toString();
    }
}
