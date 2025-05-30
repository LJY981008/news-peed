package com.example.newspeed.dto.post;

import lombok.Getter;

@Getter
public class ToggleLikeResponseDto {
    private Long postId;
    private int likeCount;
}
