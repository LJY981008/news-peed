package com.example.newspeed.service;

import com.example.newspeed.entity.Follow;
import com.example.newspeed.entity.User;
import com.example.newspeed.exception.exceptions.InvalidRequestException;
import com.example.newspeed.exception.exceptions.NotFoundException;
import com.example.newspeed.repository.follow.FollowRepository;
import com.example.newspeed.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 사용자 간 팔로우 관계를 관리하는 서비스 구현 클래스
 * 팔로우 생성 및 해제 기능을 구현합니다.
 *
 * @author 김도연
 */
@Service
@AllArgsConstructor
public class FollowServiceImpl implements FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    /**
     * 특정 사용자를 팔로우합니다.
     *
     * @param currentUserId 현재 로그인한 사용자 ID
     * @param targetEmail 팔로우할 대상 사용자의 이메일
     * @throws InvalidRequestException 자기 자신을 팔로우하거나 이미 팔로우한 사용자인 경우
     * @throws NotFoundException 대상 사용자를 찾을 수 없는 경우
     */
    @Override
    @Transactional
    public void follow(Long currentUserId, String targetEmail) {
        User targetUser = getTargetUser(targetEmail);
        Long followedUserId = targetUser.getUserId();
        
        if (currentUserId.equals(followedUserId)) {
            throw new InvalidRequestException("자기 자신은 팔로우할 수 없습니다.");
        }
        
        boolean alreadyFollowing = followRepository
                .existsByFollowingUserIdAndFollowedUserId(currentUserId, followedUserId);
        if (alreadyFollowing) {
            throw new InvalidRequestException("이미 팔로우한 사용자입니다.");
        }

        Follow follow = new Follow(currentUserId, followedUserId);
        followRepository.save(follow);
    }

    /**
     * 특정 사용자의 팔로우를 해제합니다.
     *
     * @param currentUserId 현재 로그인한 사용자 ID
     * @param targetEmail 팔로우 해제할 대상 사용자의 이메일
     * @throws NotFoundException 대상 사용자를 찾을 수 없거나 팔로우 관계가 없는 경우
     */
    @Override
    @Transactional
    public void unfollow(Long currentUserId, String targetEmail) {
        User targetUser = getTargetUser(targetEmail);
        boolean exists = followRepository.existsByFollowingUserIdAndFollowedUserId(currentUserId, targetUser.getUserId());
        if (!exists) {
            throw new NotFoundException("팔로우한 유저가 아닙니다.");
        }

        followRepository.deleteByFollowingUserIdAndFollowedUserId(currentUserId, targetUser.getUserId());
    }

    /**
     * 이메일로 사용자를 조회합니다.
     *
     * @param targetEmail 조회할 사용자의 이메일
     * @return 조회된 사용자
     * @throws NotFoundException 사용자를 찾을 수 없는 경우
     */
    private User getTargetUser(String targetEmail) {
        return userRepository.findByEmailAndDeletedFalse(targetEmail)
                .orElseThrow(() -> new NotFoundException("유저를 찾을 수 없습니다."));
    }
}
