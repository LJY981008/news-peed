package com.example.newspeed.controller;

import com.example.newspeed.dto.follow.FollowRequestDto;
import com.example.newspeed.dto.user.AuthUserDto;
import com.example.newspeed.exception.exceptions.AuthenticationException;
import com.example.newspeed.service.FollowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/news-peed/follows")
public class FollowController {
    private final FollowService followService;

    //이메일로 유저 검색해 팔로우
    @PostMapping("/follow")
    public ResponseEntity<String> followUser(@AuthenticationPrincipal AuthUserDto userDto, @Valid @RequestBody FollowRequestDto followRequest){
        Long currentUserId = userDto.getId();
        followService.follow(currentUserId, followRequest.getTargetEmail());
        return ResponseEntity.ok("success");
    }

    @DeleteMapping("/unfollow")
    public ResponseEntity<String> unfollowUser(@AuthenticationPrincipal AuthUserDto userDto, @Valid @RequestBody FollowRequestDto followRequest){
        Long currentUserId = userDto.getId();
        followService.unfollow(currentUserId, followRequest.getTargetEmail());
        return ResponseEntity.ok("success");
    }
}
