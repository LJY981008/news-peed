package com.example.newspeed.service;

import com.example.newspeed.dto.user.*;

import java.util.List;


public interface UserService {
    SignupUserResponseDto signUp(SignupUserRequestDto signupRequest);

    LoginUserResponseDto logIn(LoginUserRequestDto loginRequest);

    List<SearchUserResponseDto> search(String name, String email);

    void updateUserProfile(UpdateUserProfileRequestDto updateRequest);

    void deleteUser(DeleteUserRequestDto deleteRequest);
}
