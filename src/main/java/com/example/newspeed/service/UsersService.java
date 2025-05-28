package com.example.newspeed.service;

import com.example.newspeed.dto.*;


public interface UsersService {
    void updateUsersName(Long userId, UpdateUserNameRequestDto updateRequest);

    void updatePassword(Long userId, UpdatePasswordRequestDto updateRequest);

    void updateIntro(Long userId, UpdateIntroRequestDto updateRequest);

    void updateImage(Long userId, UpdateImageRequestDto updateRequest);

    void deleteUser(Long userId, DeleteUsersRequestDto deleteRequest);
}
