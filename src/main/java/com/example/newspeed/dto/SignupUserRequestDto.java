package com.example.newspeed.dto;

import lombok.Getter;

@Getter
public class SignupUserRequestDto {

    private String email;
    private String password;
    private String userName;
    private String intro;
    private String profileImageUrl;
}
