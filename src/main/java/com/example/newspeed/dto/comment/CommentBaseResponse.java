package com.example.newspeed.dto.comment;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class CommentBaseResponse {
    private Long commentId;
    private Long postId;
    private String userName;
    private String content;
}
