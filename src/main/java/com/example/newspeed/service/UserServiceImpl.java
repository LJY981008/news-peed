package com.example.newspeed.service;

import com.example.newspeed.dto.user.*;
import com.example.newspeed.entity.BaseEntity;
import com.example.newspeed.entity.Comment;
import com.example.newspeed.entity.Post;
import com.example.newspeed.entity.User;
import com.example.newspeed.exception.exceptions.*;
import com.example.newspeed.repository.UserRepository;
import com.example.newspeed.util.PasswordEncoder;
import com.example.newspeed.util.PasswordValidator;
import lombok.AllArgsConstructor;
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

    private static final String passwordErrorMessage = "Password must be at least 8 characters long and include uppercase, lowercase, number, and special character";
    private static final String loginFailedMessage = "Invalid email or password";

    /**
     * <p>회원 가입</p>
     *
     * @author 이현하
     * @param signupRequest 요청 DTO
     * @return SignupUserResponseDto 회원 가입한 유저 정보
     */
    @Override
    @Transactional
    public SignupUserResponseDto signUp(SignupUserRequestDto signupRequest){

        // 중복된 이메일로 가입 시 에러 반환
        if(userRepository.existsByEmail(signupRequest.getEmail())){
            throw new DuplicateEmailException("Email already in use");
        }
        // 비밀번호 valid 확인
        validatePassword(signupRequest.getPassword());

        User user = new User(
                signupRequest.getEmail(),
                passwordEncoder.encode(signupRequest.getPassword()),
                signupRequest.getUserName(),
                signupRequest.getIntro(),
                signupRequest.getProfileImageUrl());

        User savedUser = userRepository.save(user);

        return new SignupUserResponseDto(savedUser);
    }

    /**
     * <p>로그인</p>
     *
     * @author 이현하
     * @param loginRequest 요청 DTO
     * @return LoginUserResponseDto 로그인한 유저 정보
     */
    @Override
    public LoginUserResponseDto logIn(LoginUserRequestDto loginRequest){

        // 존재하지 않는 이메일일 때 에러 반환
        User findUser = userRepository.findByEmailAndDeletedFalse(loginRequest.getEmail())
                .orElseThrow(() -> new LoginFailedException(loginFailedMessage));

        // 비밀번호 일치하지 않을 때 에러 반환
        if (!passwordEncoder.matches(loginRequest.getPassword(), findUser.getPassword())) {
            throw new LoginFailedException(loginFailedMessage);
        }

        return new LoginUserResponseDto(findUser);
    }


    /**
     * <p>유저 검색</p>
     *
     * @author 이현하
     * @param name 유저 이름, email 유저 이메일
     * @return List<SearchUserResponseDto> 검색된 유저 정보 리스트
     */
    @Override
    public List<SearchUserResponseDto> search(String name, String email){

        if(name != null && email != null){
            // name, email 동시 검색 비허용

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Search by both name and email is not supported");

        } else if(name != null){
            // name으로 유저 검색, 검색 단어가 포함된 모든 이름 검색

            List<User> userList = userRepository.findByUserNameContainingAndDeletedFalse(name);

            if (userList == null || userList.isEmpty()) {
                throw new NoResultFoundException("No search results found for the user name");
            }

            return userList.stream()
                    .map(SearchUserResponseDto::new)
                    .collect(Collectors.toList());

        } else if(email != null){
            // email로 유저 검색

            User user = userRepository.findByEmailAndDeletedFalse(email)
                    .orElseThrow(() -> new NoResultFoundException("No search results found for the email"));
            return List.of(new SearchUserResponseDto(user));

        } else {
            // param 입력되지 않으면 전체 유저 리스트 검색

            List<User> userList = userRepository.findByDeletedFalse();

            if (userList == null || userList.isEmpty()) {
                throw new NoResultFoundException("No user list found");
            }

            return userList.stream()
                    .map(SearchUserResponseDto::new)
                    .collect(Collectors.toList());
        }
    }

    /**
     * <p>프로필 수정</p>
     *
     * @author 김도연
     * @param updateRequest 수정요청DTO
     * @return 없음
     */
    @Override
    @Transactional
    public void updateUserProfile(UpdateUserProfileRequestDto updateRequest){ //profile 수정 통합
        User user = userRepository.findByEmailAndDeletedFalse(updateRequest.getEmail())
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
            //password 형식검증
            validatePassword(updateRequest.getPassword());
            validatePassword(updateRequest.getNewPassword());

            // password matching
            if(!passwordEncoder.matches(updateRequest.getPassword(), user.getPassword())) throw new LoginFailedException(loginFailedMessage);

            user.setPassword(passwordEncoder.encode(updateRequest.getNewPassword()));
        }
    }

    /**
     * <p>프로필 삭제</p>
     *
     * @author 김도연
     * @param deleteRequest 삭제요청DTO
     * @return 없음
     */
    @Override
    @Transactional
    public void deleteUser(DeleteUserRequestDto deleteRequest) {
        User user = userRepository.findByEmailAndDeletedFalse(deleteRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User Not Found"));
        //password 형식검증
        validatePassword(deleteRequest.getPassword());

        // password matching
        if(!passwordEncoder.matches(deleteRequest.getPassword(), user.getPassword())) throw new LoginFailedException(loginFailedMessage);

        user.softDelete();
        logicalDeleteCascade(user);
        userRepository.save(user);
    }

    private void logicalDeleteCascade(User user){
        List<Post> posts = user.getPosts();
        posts.forEach(Post::softDelete);

        List<Comment> comments = user.getComments();
        comments.forEach(BaseEntity::softDelete);
    }

    private void validatePassword(String password){
        if(!PasswordValidator.isValid(password)) throw new InvalidPasswordException(passwordErrorMessage);
    }
}
