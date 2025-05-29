package com.example.newspeed.dto.user;

import com.example.newspeed.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SignupUserResponseDto {

    private final Long id;
    private final String email;
    private final String userName;
    private final String intro;
    private final String profileImageUrl;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public SignupUserResponseDto(User user){
        this.id = user.getUserId();
        this.email = user.getEmail();
        this.userName = user.getUserName();
        this.intro = user.getIntro();
        this.profileImageUrl = user.getProfileImageUrl();
        this.createdAt = user.getCreatedAt();
        this.modifiedAt = user.getModifiedAt();
    }
}
