package com.example.newspeed.service;

import com.example.newspeed.dto.*;


public interface UsersService {
    SignupUserResponseDto signUp(SignupUserRequestDto signupRequest);
    UsersResponseDto updateUsersName(Long userId, UpdateUserNameRequestDto updateRequest);
}
