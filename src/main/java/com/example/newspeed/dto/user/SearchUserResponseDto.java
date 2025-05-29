package com.example.newspeed.dto.user;

import com.example.newspeed.entity.Users;
import lombok.Getter;

@Getter
public class SearchUserResponseDto {

    private final Long id;
    private final String email;
    private final String userName;
    private final String intro;
    private final String profileImageUrl;

    public SearchUserResponseDto(Users user){
        this.id = user.getUserId();
        this.email = user.getEmail();
        this.userName = user.getUserName();
        this.intro = user.getIntro();
        this.profileImageUrl = user.getProfileImageUrl();
    }
}
