package com.example.newspeed.service;

/**
 * 사용자 간 팔로우 관계를 관리하는 서비스 인터페이스
 * 팔로우 생성 및 해제 기능을 제공합니다.
 *
 * @author 김도연
 */
public interface FollowService {
    /**
     * 특정 사용자를 팔로우합니다.
     *
     * @param currentUserId 현재 로그인한 사용자 ID
     * @param targetEmail 팔로우할 대상 사용자의 이메일
     */
    void follow(Long currentUserId, String targetEmail);

    /**
     * 특정 사용자의 팔로우를 해제합니다.
     *
     * @param currentUserId 현재 로그인한 사용자 ID
     * @param targetEmail 팔로우 해제할 대상 사용자의 이메일
     */
    void unfollow(Long currentUserId, String targetEmail);
}
