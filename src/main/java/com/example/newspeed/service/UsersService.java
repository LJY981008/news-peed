package com.example.newspeed.service;

import com.example.newspeed.dto.user.*;

import java.util.List;


public interface UsersService {
    SignupUserResponseDto signUp(SignupUserRequestDto signupRequest);

    LoginUserResponseDto logIn(LoginUserRequestDto loginRequest);

    List<SearchUserResponseDto> search(String name, String email);

    void updateUsersName(UpdateUserNameRequestDto updateRequest);

    void updatePassword(UpdatePasswordRequestDto updateRequest);

    void updateIntro(UpdateIntroRequestDto updateRequest);

    void updateImage(UpdateImageRequestDto updateRequest);

    void deleteUser(DeleteUsersRequestDto deleteRequest);
}
