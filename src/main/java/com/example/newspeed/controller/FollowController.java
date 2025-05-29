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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/news-peed/follows")
public class FollowController {
    private final FollowService followService;

    //이메일로 유저 검색해 팔로우
    @PostMapping
    public ResponseEntity<String> followUser(@AuthenticationPrincipal AuthUserDto userDto, @Valid @RequestBody FollowRequestDto followRequest){
        if(userDto == null){
            throw new AuthenticationException("인증되지 않은 사용자입니다.");
        }

        Long currentUserId = userDto.getId();
        followService.follow(currentUserId, followRequest.getTargetEmail());
        return ResponseEntity.ok("success");
    }
}
