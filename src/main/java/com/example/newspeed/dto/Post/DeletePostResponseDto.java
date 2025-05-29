package com.example.newspeed.dto.post;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class DeletePostResponseDto {
    private final String message;
    private final String pagenationUrl;

    public DeletePostResponseDto() {
        this.message = "게시글이 성공적으로 삭제되었습니다.";
        this.pagenationUrl = "url";
    }


}
