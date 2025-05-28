package com.example.newspeed.service;

import com.example.newspeed.dto.SignupUserRequestDto;
import com.example.newspeed.dto.SignupUserResponseDto;
import com.example.newspeed.dto.UpdateUserNameRequestDto;
import com.example.newspeed.dto.UsersResponseDto;
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
    public SignupUserResponseDto signUp(SignupUserRequestDto signupRequest){
        Users user = new Users(
                signupRequest.getEmail(), signupRequest.getPassword(), signupRequest.getUserName(),
                signupRequest.getIntro(), signupRequest.getProfileImageUrl());

        Users savedUser = usersRepository.save(user);

        return new SignupUserResponseDto(savedUser);
    }

    @Override
    @Transactional
    public UsersResponseDto updateUsersName(Long userId, UpdateUserNameRequestDto updateRequest) {
        return null;
    }
}
