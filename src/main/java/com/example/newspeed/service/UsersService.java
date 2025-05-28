package com.example.newspeed.service;

import com.example.newspeed.dto.*;

import java.util.List;


public interface UsersService {
    SignupUserResponseDto signUp(SignupUserRequestDto signupRequest);
    LoginUserResponseDto logIn(LoginUserRequestDto loginRequest);
    List<SearchUserResponseDto> search(String name, String email);
    UsersResponseDto updateUsersName(Long userId, UpdateUserNameRequestDto updateRequest);
}
