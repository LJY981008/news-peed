package com.example.newspeed.dto.post;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class DeletePostRequestDto {
    private final Long postId;
}
