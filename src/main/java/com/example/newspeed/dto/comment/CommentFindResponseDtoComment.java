package com.example.newspeed.dto.comment;

import lombok.Getter;

@Getter
public class CommentFindResponseDtoComment extends CommentBaseResponse {
    //private int likeCount;
    private String createdAt;
    private String modifiedAt;
}
