package com.example.newspeed.dto.Post;

import lombok.Getter;

@Getter
public class UpdatePostRequestDto {

    private final String title;
    private final String contents;
    private final String password;

    public UpdatePostRequestDto(String title, String contents, String password) {
        this.title = title;
        this.contents = contents;
        this.password = password;
    }
}
