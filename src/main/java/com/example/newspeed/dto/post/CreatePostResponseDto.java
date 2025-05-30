package com.example.newspeed.dto.post;

import lombok.Getter;



@Getter
public class CreatePostResponseDto {
    private final String  message;
    private final String postPageUrl;

    public CreatePostResponseDto(String message,String postPageUrl) {
        this.message = message;
        this.postPageUrl = postPageUrl;
    }
}
