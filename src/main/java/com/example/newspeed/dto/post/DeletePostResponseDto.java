package com.example.newspeed.dto.post;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class DeletePostResponseDto {
    private final String message;
    private final String postPageUrl;

}
