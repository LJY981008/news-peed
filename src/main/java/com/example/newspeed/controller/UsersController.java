package com.example.newspeed.controller;

import com.example.newspeed.dto.SignupUserRequestDto;
import com.example.newspeed.dto.SignupUserResponseDto;
import com.example.newspeed.dto.UpdateUserNameRequestDto;
import com.example.newspeed.service.UsersService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/news-peed")
public class UsersController {
    private final UsersService usersService;

    /**
     * 회원 가입
     * @param signupRequest
     * { email, password, userName, intro, profileImageUrl }
     * @return SignupUserResponseDto
     * { id, email, password, userName, intro, profileImageUrl, createdAt, updatedAt }
     */
    @PostMapping("/sign-up")
    public ResponseEntity<SignupUserResponseDto> signUp(@Valid @RequestBody SignupUserRequestDto signupRequest){
        SignupUserResponseDto signUpResponseDto = usersService.signUp(signupRequest);
        return new ResponseEntity<>(signUpResponseDto, HttpStatus.CREATED);
    }


    @PutMapping("/modify-name")
    public ResponseEntity<String> modifyName(@Valid @RequestBody UpdateUserNameRequestDto updateRequest, HttpServletRequest request){
        Long userId = extractUserIdFromCookie(request);
        usersService.updateUsersName(userId, updateRequest);
        return ResponseEntity.ok("success");
    }

    private Long extractUserIdFromCookie(HttpServletRequest request){
        if(request.getCookies() == null) throw new RuntimeException("there's no cookie");
        for(Cookie cookie : request.getCookies()){
            if("userId".equals(cookie.getName())) return Long.valueOf(cookie.getValue());
        }
        throw new RuntimeException("there's no cookie");
    }
}
