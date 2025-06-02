package com.example.newspeed.service;

import com.example.newspeed.dto.user.*;

import java.util.List;

/**
 * 사용자 관련 비즈니스 로직을 처리하는 서비스 인터페이스
 * 사용자의 회원가입, 로그인, 프로필 관리 기능을 제공합니다.
 *
 * @author 이현하, 김도연
 */
public interface UserService {
    /**
     * 새로운 사용자를 등록합니다.
     *
     * @param signupRequest 회원가입 요청 정보
     * @return 회원가입 결과 정보
     */
    SignupUserResponseDto signUp(SignupUserRequestDto signupRequest);

    /**
     * 사용자 로그인을 처리합니다.
     *
     * @param loginRequest 로그인 요청 정보
     * @return 로그인 결과 정보 (JWT 토큰 포함)
     */
    LoginUserResponseDto logIn(LoginUserRequestDto loginRequest);

    /**
     * 사용자를 검색합니다.
     *
     * @param name 검색할 사용자 이름
     * @param email 검색할 사용자 이메일
     * @return 검색된 사용자 목록
     */
    List<SearchUserResponseDto> search(String name, String email);

    /**
     * 사용자 프로필을 수정합니다.
     *
     * @param updateRequest 수정할 프로필 정보
     */
    void updateUserProfile(UpdateUserProfileRequestDto updateRequest);

    /**
     * 사용자 계정을 삭제합니다.
     *
     * @param deleteRequest 삭제 요청 정보
     */
    void deleteUser(DeleteUserRequestDto deleteRequest);
}
