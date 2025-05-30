package com.example.newspeed.dto.post;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * <p>게시글 생성 요청 DTO</p>
 *
 * @author 윤희준
 */
@RequiredArgsConstructor
@Getter
public class CreatePostRequestDto {
    private final String title;
    private final String content;
    private final String imageUrl;

}
