package com.example.newspeed.dto.comment;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentUpdateResponseDto {
    private Long id;
    private String writer;
    private String prevContent;
    private String currentContent;
    private String createdAt;
    private String modifiedAt;
}
