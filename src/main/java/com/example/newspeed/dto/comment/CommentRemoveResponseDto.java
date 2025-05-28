package com.example.newspeed.dto.comment;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentRemoveResponseDto {
    private Long id;
    private String writer;
    private String content;
}
