package com.example.newspeed.service;

import com.example.newspeed.dto.*;
import com.example.newspeed.entity.Users;
import com.example.newspeed.repository.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<SearchUserResponseDto> search(String name, String email){
        if(name != null && email != null){
            // name, email 동시 검색 비허용
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name, email 동시 검색은 허용되지 않습니다.");
        } else if(name != null){
            // name으로 유저 검색
            Users user = usersRepository.findByUserName(name)
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + name));
            return List.of(new SearchUserResponseDto(user));
        } else if(email != null){
            // email로 유저 검색
            Users user = usersRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + email));
            return List.of(new SearchUserResponseDto(user));
        } else {
            // param 입력되지 않으면 전체 유저 리스트 검색
            List<Users> usersList = usersRepository.findAll();
            return usersList.stream()
                    .map(SearchUserResponseDto::new)
                    .collect(Collectors.toList());
        }
    }

    @Override
    @Transactional
    public void updateUserProfile(UpdateUserProfileRequestDto updateRequest){ //profile 수정 통합 - password 암호화 미구현 상태
        Users user = usersRepository.findByEmail(updateRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User Not Found"));
        if(updateRequest.getUserName() != null){
            user.setUserName(updateRequest.getUserName());
        }
        if(updateRequest.getIntro() != null){
            user.setIntro(updateRequest.getIntro());
        }
        if(updateRequest.getProfileImage() != null){
            user.setProfileImageUrl(updateRequest.getProfileImage());
        }
        if(updateRequest.getPassword() != null && updateRequest.getNewPassword() != null){
            user.setPassword(updateRequest.getNewPassword());
        }
    }

    @Override
    @Transactional
    public void deleteUser(DeleteUsersRequestDto deleteRequest) {
        Users user = usersRepository.findByEmail(deleteRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User Not Found"));
        usersRepository.delete(user); //Bcrypt 암호화 미적용 상태 - 우선 어떤 비밀번호든 상관없이 삭제 요청시 삭제(암호화 적용 시 구현 예정)
    }
}
