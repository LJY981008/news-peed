package com.example.newspeed.service;

import com.example.newspeed.dto.*;

import java.util.List;


public interface UsersService {
    SignupUserResponseDto signUp(SignupUserRequestDto signupRequest);
    LoginUserResponseDto logIn(LoginUserRequestDto loginRequest);
    List<SearchUserResponseDto> search(String name, String email);

    void updateUsersName(Long userId, UpdateUserNameRequestDto updateRequest);

    void updatePassword(Long userId, UpdatePasswordRequestDto updateRequest);

    void updateIntro(Long userId, UpdateIntroRequestDto updateRequest);

    void updateImage(Long userId, UpdateImageRequestDto updateRequest);

    void deleteUser(Long userId, DeleteUsersRequestDto deleteRequest);
}
