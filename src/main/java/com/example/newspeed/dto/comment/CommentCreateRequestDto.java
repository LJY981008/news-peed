package com.example.newspeed.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

/**
 * <p>댓글 생성 요청</p>
 *
 * @author 이준영
 */
@Getter
public class CommentCreateRequestDto {

    @NotBlank(message = "Content is Empty")
    @Size(min = 1, max = 100)
    private String content;
}
