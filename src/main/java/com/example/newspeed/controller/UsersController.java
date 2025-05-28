package com.example.newspeed.controller;

import com.example.newspeed.dto.*;
import com.example.newspeed.service.UsersService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /**
     * 로그인
     * @param loginRequest
     * { email, password }
     * @return LoginUserResponseDto
     * { id, email, userName, intro, profileImageUrl, createdAt, updatedAt }
     */
    @GetMapping("/log-in")
    public ResponseEntity<LoginUserResponseDto> lonIn(@Valid @RequestBody LoginUserRequestDto loginRequest){
        LoginUserResponseDto loginResponseDto = usersService.logIn(loginRequest);
        return new ResponseEntity<>(loginResponseDto, HttpStatus.OK);
    }

    /**
     * 유저 검색 (전체 검색, 이름 검색, 이메일 검색)
     * @param name 이름
     * @param email 이메일
     * @return SearchUserResponseDto
     * { id, email, userName, intro, profileImageUrl }
     */
    @GetMapping("/search")
    public ResponseEntity<List<SearchUserResponseDto>> search(@RequestParam(required = false) String name,
                                                        @RequestParam(required = false) String email){
        List<SearchUserResponseDto> searchResponseList = usersService.search(name, email);

        return new ResponseEntity<>(searchResponseList, HttpStatus.OK);
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
