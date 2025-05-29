package com.example.newspeed.dto.comment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class CommentUpdateResponseDtoComment extends CommentBaseResponse {
    private String createdAt;
    private String modifiedAt;

    @Setter
    private String prevContent;
}
