package com.example.newspeed.dto.post;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class DeletePostResponseDto {
    private final String message;
    private final String pagenationUrl;

    public DeletePostResponseDto(String message) {
        this.message = message;
        this.pagenationUrl = "url";//페이지네이션적용후 수정
    }
}
