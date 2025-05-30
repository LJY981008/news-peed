package com.example.newspeed.service;

import com.example.newspeed.dto.user.*;
import com.example.newspeed.entity.User;
import com.example.newspeed.exception.exceptions.*;
import com.example.newspeed.repository.UserRepository;
import com.example.newspeed.util.PasswordEncoder;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;


    @Override
    @Transactional
    public SignupUserResponseDto signUp(SignupUserRequestDto signupRequest){

        // 중복된 이메일로 가입 시 에러 반환
        if(userRepository.existsByEmail(signupRequest.getEmail())){
            throw new DuplicateEmailException("Email already in use");
        }

        // 비밀번호 encoding
        final String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

        User user = new User(
                signupRequest.getEmail(), encodedPassword, signupRequest.getUserName(),
                signupRequest.getIntro(), signupRequest.getProfileImageUrl());

        User savedUser = userRepository.save(user);

        return new SignupUserResponseDto(savedUser);
    }


    @Override
    public LoginUserResponseDto logIn(LoginUserRequestDto loginRequest){

        // 존재하지 않는 이메일일 때 에러 반환
        User findUser = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new LoginFailedException("Invalid email or password"));

        // 비밀번호 일치하지 않을 때 에러 반환
        if (!passwordEncoder.matches(loginRequest.getPassword(), findUser.getPassword())) {
            throw new LoginFailedException("Invalid email or password");
        }

        return new LoginUserResponseDto(findUser);
    }


    @Override
    public List<SearchUserResponseDto> search(String name, String email){

        if(name != null && email != null){
            // name, email 동시 검색 비허용

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Search by both name and email is not supported");

        } else if(name != null){
            // name으로 유저 검색

            List<User> userList = userRepository.findByUserName(name);

            if (userList == null || userList.isEmpty()) {
                throw new NoResultFoundException("No search results found for the user name");
            }

            return userList.stream()
                    .map(SearchUserResponseDto::new)
                    .collect(Collectors.toList());

        } else if(email != null){
            // email로 유저 검색

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new NoResultFoundException("No search results found for the email"));
            return List.of(new SearchUserResponseDto(user));

        } else {
            // param 입력되지 않으면 전체 유저 리스트 검색

            List<User> userList = userRepository.findAll();

            if (userList == null || userList.isEmpty()) {
                throw new NoResultFoundException("No user list found");
            }

            return userList.stream()
                    .map(SearchUserResponseDto::new)
                    .collect(Collectors.toList());
        }
    }


    @Override
    @Transactional
    public void updateUserProfile(UpdateUserProfileRequestDto updateRequest){ //profile 수정 통합 - password 암호화 미구현 상태
        User user = userRepository.findByEmail(updateRequest.getEmail())
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
    public void deleteUser(DeleteUserRequestDto deleteRequest) {
        User user = userRepository.findByEmail(deleteRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User Not Found"));
        userRepository.delete(user); //Bcrypt 암호화 미적용 상태 - 우선 어떤 비밀번호든 상관없이 삭제 요청시 삭제(암호화 적용 시 구현 예정)
    }
}
