package com.example.newspeed.dto;

import com.example.newspeed.entity.Users;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class LoginUserResponseDto {

    private final Long id;
    private final String email;
    private final String userName;
    private final String intro;
    private final String profileImageUrl;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public LoginUserResponseDto(Users user){
        this.id = user.getUserId();
        this.email = user.getEmail();
        this.userName = user.getUserName();
        this.intro = user.getIntro();
        this.profileImageUrl = user.getProfileImageUrl();
        this.createdAt = user.getCreatedAt();
        this.modifiedAt = user.getModifiedAt();
    }
}
