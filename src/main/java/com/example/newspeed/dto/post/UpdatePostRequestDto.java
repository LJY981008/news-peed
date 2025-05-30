package com.example.newspeed.dto.post;

import lombok.Getter;

/**
 * 게시글 수정 관련 Dto
 * @author 김태현
 */

@Getter
public class UpdatePostRequestDto {

    private final String title;
    private final String contents;

    public UpdatePostRequestDto(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }
}
