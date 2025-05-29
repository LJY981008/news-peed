package com.example.newspeed.dto.comment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class CommentUpdateResponseDto {
    private Long id;
    private String writer;
    private String currentContent;
    private String createdAt;
    private String modifiedAt;

    @Setter
    private String prevContent;
}
