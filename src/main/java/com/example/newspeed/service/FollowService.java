package com.example.newspeed.service;

public interface FollowService {
    void follow(Long currentUserId, String targetEmail);

    void unfollow(Long currentUserId, String targetEmail);
}
