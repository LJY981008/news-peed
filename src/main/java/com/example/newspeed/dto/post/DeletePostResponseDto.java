package com.example.newspeed.dto.post;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * <p>게시글 삭제 응답 DTO</p>
 *
 * author 윤희준
 */
@RequiredArgsConstructor
@Getter
public class DeletePostResponseDto {
    private final String message;
    private final String postPageUrl;

}
