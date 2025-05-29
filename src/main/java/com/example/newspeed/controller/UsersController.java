package com.example.newspeed.controller;

import com.example.newspeed.config.JwtUtil;
import com.example.newspeed.dto.*;
import com.example.newspeed.enums.UserRole;
import com.example.newspeed.service.UsersService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/news-peed/users")
public class UsersController {
    private final UsersService usersService;


    /* 토큰 설명
        private final JwtUtil jwtUtil;
        토큰 생성 : jwtUtil.createToken(1L, "LJY", UserRole.USER);
        토큰 생성 메서드 프로퍼티 타입 : public String createToken(Long userId, String name, UserRole userRole)
        토큰 생성 후 반환 :
        return ResponseEntity.status(HttpStatus.OK)
                    .header("Authorization", token)
                    .body(responseDto);

        인가된 객체 호출
        Optional<Authentication> authentication = Optional.ofNullable(
                SecurityContextHolder.getContext().getAuthentication()
        );
        if(authentication.isPresent() && authentication.get().getPrincipal() instanceof AuthUserDto){
            AuthUserDto authUserDto = (AuthUserDto) authentication.get().getPrincipal();
            Long userId = authUserDto.getId();
            String email = authUserDto.getEmail();
        }
     */

    /**
     * 회원 가입
     * @param signupRequest
     * { email, password, userName, intro, profileImageUrl }
     * @return SignupUserResponseDto
     * { id, email, password, userName, intro, profileImageUrl, createdAt, updatedAt }
     */
    @PostMapping("/signup")
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
    @GetMapping("/login")
    public ResponseEntity<LoginUserResponseDto> logIn(@Valid @RequestBody LoginUserRequestDto loginRequest){
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
    public ResponseEntity<String> modifyName(@Valid @RequestBody UpdateUserNameRequestDto updateRequest){
        usersService.updateUsersName(updateRequest);
        return ResponseEntity.ok("success");
    }

    @PutMapping("/modify-password")
    public ResponseEntity<String> modifyPassword(@Valid @RequestBody UpdatePasswordRequestDto updateRequest){
        usersService.updatePassword(updateRequest);
        return ResponseEntity.ok("success");
    }

    @PutMapping("/modify-intro")
    public ResponseEntity<String> modifyIntro(@Valid @RequestBody UpdateIntroRequestDto updateRequest){
        usersService.updateIntro(updateRequest);
        return ResponseEntity.ok("success");
    }

    @PutMapping("/modify-image")
    public ResponseEntity<String> modiyImage(@Valid @RequestBody UpdateImageRequestDto updateRequest){
        usersService.updateImage(updateRequest);
        return ResponseEntity.ok("success");
    }

    @DeleteMapping("/quit")
    public ResponseEntity<String> quitUser(@Valid @RequestBody DeleteUsersRequestDto deleteRequest){
        usersService.deleteUser(deleteRequest);
        return ResponseEntity.ok("success");
    }
}
