package com.example.newspeed.dto.comment;

import com.example.newspeed.entity.Comment;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CommentFindResponseDto {

    private Long id;
    private String content;
    //private String writer;
    //private int likeCount;
    private String createdAt;
    private String modifiedAt;


}
