package com.example.newspeed.dto.comment;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class CommentCreateRequestDto {

    @NonNull
    String content;
}
