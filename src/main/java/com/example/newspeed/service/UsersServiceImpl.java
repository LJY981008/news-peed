package com.example.newspeed.service;

import com.example.newspeed.dto.*;
import com.example.newspeed.repository.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UsersServiceImpl implements UsersService{
    private final UsersRepository usersRepository;

    @Override
    @Transactional
    public UsersResponseDto updateUsersName(Long userId, UpdateUserNameRequestDto updateRequest) {
        return null;
    }

    @Override
    @Transactional
    public UsersResponseDto updatePassword(Long userId, UpdatePasswordRequestDto updateRequest){
        return null;
    }

    @Override
    @Transactional
    public UsersResponseDto updateIntro(Long userId, UpdateIntroRequestDto updateRequest) {
        return null;
    }

    @Override
    @Transactional
    public UsersResponseDto updateImage(Long userId, UpdateImageRequestDto updateRequest) {
        return null;
    }

    @Override
    @Transactional
    public void deleteUser(Long userId, DeleteUsersRequestDto deleteRequest) {

    }
}
