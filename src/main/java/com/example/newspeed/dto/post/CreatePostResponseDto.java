package com.example.newspeed.dto.post;

import com.example.newspeed.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
public class CreatePostResponseDto {
    private final String  message;
    private final String pagenationUrl;

    public CreatePostResponseDto() {
        this.message = "게시글 생성에 성공했습니다";
        this.pagenationUrl = "PostPageUrl";
    }
}
