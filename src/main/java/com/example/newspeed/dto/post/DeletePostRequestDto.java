package com.example.newspeed.dto.post;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * <p>게시글 삭제 요청 DTO</p>
 *
 * @author 윤희준
 */
@RequiredArgsConstructor
@Getter
public class DeletePostRequestDto {

    private final Long postId;
}
