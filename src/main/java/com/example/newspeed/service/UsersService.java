package com.example.newspeed.service;

import com.example.newspeed.dto.*;


public interface UsersService {
    UsersResponseDto updateUsersName(Long userId, UpdateUserNameRequestDto updateRequest);

    UsersResponseDto updatePassword(Long userId, UpdatePasswordRequestDto updateRequest);

    UsersResponseDto updateIntro(Long userId, UpdateIntroRequestDto updateRequest);

    UsersResponseDto updateImage(Long userId, UpdateImageRequestDto updateRequest);

    void deleteUser(Long userId, DeleteUsersRequestDto deleteRequest);
}
