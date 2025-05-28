package com.example.newspeed.service;

import com.example.newspeed.dto.*;
import com.example.newspeed.entity.Users;
import com.example.newspeed.repository.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
    public LoginUserResponseDto logIn(LoginUserRequestDto loginRequest){
        Users findUser = usersRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + loginRequest.getEmail()));

        if(!findUser.getPassword().equals(loginRequest.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효한 회원 정보가 존재하지 않습니다.");
        }

        return new LoginUserResponseDto(findUser);
    }

    @Override
    @Transactional
    public UsersResponseDto updateUsersName(Long userId, UpdateUserNameRequestDto updateRequest) {
        return null;
    }
}
