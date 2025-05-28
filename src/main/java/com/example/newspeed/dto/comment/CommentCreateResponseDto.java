package com.example.newspeed.dto.comment;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentCreateResponseDto {

    private Long id;
    private Long postId;
    private String writer;
    private String content;
}
