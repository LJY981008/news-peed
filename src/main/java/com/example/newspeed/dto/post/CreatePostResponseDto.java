package com.example.newspeed.dto.post;

import lombok.Getter;


/**
 * <p>게시글 생성 응답 DTO</p>
 *
 * @author 윤희준
 */
@Getter
public class CreatePostResponseDto {
    private final String  message;
    private final String postPageUrl;

    public CreatePostResponseDto(String message,String postPageUrl) {
        this.message = message;
        this.postPageUrl = postPageUrl;
    }
}
