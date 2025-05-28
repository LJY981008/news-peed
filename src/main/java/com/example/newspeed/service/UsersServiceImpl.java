package com.example.newspeed.service;

import com.example.newspeed.dto.UpdateUserNameRequestDto;
import com.example.newspeed.dto.UsersResponseDto;
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
}
