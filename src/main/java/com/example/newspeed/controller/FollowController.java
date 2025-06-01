package com.example.newspeed.controller;

import com.example.newspeed.constant.Const;
import com.example.newspeed.dto.comment.CommentCreateRequestDto;
import com.example.newspeed.dto.comment.CommentCreateResponseDto;
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

/**
 * <p>follow controller</p>
 *
 * @author 김도연
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(Const.FOLLOWS_URL)
public class FollowController {
    private final FollowService followService;

    /**
     * <p>유저 팔로우</p>
     *
     * @param userDto    로그인된 사용자 정보
     * @param followRequest {@link FollowRequestDto} 요청 DTO
     * @return "success"    반환 Message
     */
    @PostMapping("/follow")
    public ResponseEntity<String> followUser(
            @AuthenticationPrincipal AuthUserDto userDto,
            @Valid @RequestBody FollowRequestDto followRequest
    ){
        Long currentUserId = userDto.getId();
        followService.follow(currentUserId, followRequest.getTargetEmail());
        return ResponseEntity.ok("success");
    }

    /**
     * <p>유저 언팔로우</p>
     *
     * @param userDto    로그인된 사용자 정보
     * @param followRequest {@link FollowRequestDto} 요청 DTO
     * @return "success"    반환 Message
     */
    @DeleteMapping("/unfollow")
    public ResponseEntity<String> unfollowUser(
            @AuthenticationPrincipal AuthUserDto userDto,
            @Valid @RequestBody FollowRequestDto followRequest
    ){
        Long currentUserId = userDto.getId();
        followService.unfollow(currentUserId, followRequest.getTargetEmail());
        return ResponseEntity.ok("success");
    }
}
