package com.example.newspeed.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class CommentCreateRequestDto {

    @NotBlank(message = "Content is Empty")
    String content;
}
