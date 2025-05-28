package com.example.newspeed.service;

import com.example.newspeed.dto.*;
import com.example.newspeed.entity.Users;
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
    public void updateUsersName(Long userId, UpdateUserNameRequestDto updateRequest) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자 찾을 수 없음"));
        user.setUserName(updateRequest.getUserName());
    }

    @Override
    @Transactional
    public void updatePassword(Long userId, UpdatePasswordRequestDto updateRequest){
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자 찾을 수 없음"));
        user.setPassword(updateRequest.getNewPassword());
    }

    @Override
    @Transactional
    public void updateIntro(Long userId, UpdateIntroRequestDto updateRequest) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자 찾을 수 없음"));
        user.setIntro(updateRequest.getIntro());
    }

    @Override
    @Transactional
    public void updateImage(Long userId, UpdateImageRequestDto updateRequest) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자 찾을 수 없음"));
        user.setProfileImageUrl(updateRequest.getProfileImage());
    }

    @Override
    @Transactional
    public void deleteUser(Long userId, DeleteUsersRequestDto deleteRequest) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자 찾을 수 없음"));
        usersRepository.delete(user);
    }
}
