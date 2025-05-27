package com.example.newspeed.service;

import com.example.newspeed.dto.*;


public interface UsersService {
    UsersResponseDto updateUsersName(Long userId, UpdateUserNameRequestDto updateRequest);
}
