package com.example.newspeed.dto.post;

import lombok.Getter;

@Getter
public class CreatePostRequestDto {
    private final String title;
    private final String content;
    private final String imageUrl;

    public CreatePostRequestDto(String title, String content, String imageUrl) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
    }
}
