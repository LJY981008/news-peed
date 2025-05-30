package com.example.newspeed.service;

/**
 * 팔로우 서비스 인터페이스
 *
 * @author 김도연
 */
public interface FollowService {
    void follow(Long currentUserId, String targetEmail);

    void unfollow(Long currentUserId, String targetEmail);
}
