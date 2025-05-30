package com.example.newspeed.dto.post;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CreatePostRequestDto {
    private final String title;
    private final String content;
    private final String imageUrl;

}
