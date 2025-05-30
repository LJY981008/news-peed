package com.example.newspeed.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

/**
 * <p>댓글 수정 요청</p>
 *
 * @author 이준영
 */
@Getter
public class CommentUpdateRequestDto {

    @NotBlank(message = "Comment is Empty")
    private String content;
}
