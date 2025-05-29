package com.example.newspeed.service;

import com.example.newspeed.entity.Follow;
import com.example.newspeed.entity.User;
import com.example.newspeed.exception.exceptions.InvalidRequestException;
import com.example.newspeed.exception.exceptions.NotFoundException;
import com.example.newspeed.repository.FollowRepository;
import com.example.newspeed.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class FollowServiceImpl implements FollowService{
    private final FollowRepository followRepository;
    private final UserRepository usersRepository;
    @Override
    @Transactional
    public void follow(Long currentUserId, String targetEmail) {
        User targetUser = usersRepository.findByEmail(targetEmail)
                .orElseThrow(() -> new NotFoundException("유저를 찾을 수 없습니다."));
        Long followedUserId = targetUser.getUserId();
        //팔로우 하려는 유저와 사용자가 동일 계정일 경우 예외처리
        if(currentUserId.equals(followedUserId)) throw new InvalidRequestException("자기 자신은 팔로우할 수 없습니다.");
        //팔로우 여부 검색
        boolean alreadyFollowing = followRepository
                .existsByFollowingUserIdAndFollowedUserId(currentUserId, followedUserId);
        //팔로우 된 유저를 다시 팔로우 하는 경우 예외처리
        if(alreadyFollowing) throw new InvalidRequestException("이미 팔로우한 사용자입니다.");

        Follow follow = new Follow(currentUserId, followedUserId);
        followRepository.save(follow);
    }

    @Override
    @Transactional
    public void unfollow(Long currentUserId, String targetEmail){
        User targetUser = usersRepository.findByEmail(targetEmail)
                .orElseThrow(() -> new NotFoundException("유저를 찾을 수 없습니다."));
        //팔로우 하지 않은 관계에서 삭제 요청 시 예외처리
        boolean exists = followRepository.existsByFollowingUserIdAndFollowedUserId(currentUserId, targetUser.getUserId());
        if(!exists) throw new NotFoundException("팔로우한 유저가 아닙니다.");

        followRepository.deleteByFollowingUserIdAndFollowedUserId(currentUserId, targetUser.getUserId());
    }
}
