package com.example.newspeed.controller;

import com.example.newspeed.config.JwtUtil;
import com.example.newspeed.constant.Const;
import com.example.newspeed.dto.user.*;
import com.example.newspeed.enums.UserRole;
import com.example.newspeed.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(Const.USER_URL)
public class UserController {
    private final UserService usersService;
    private final JwtUtil jwtUtil;


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
     * <p>회원 가입</p>
     *
     * @author 이현하
     * @param signupRequest 요청 DTO
     * @return SignupUserResponseDto 회원 가입한 유저 정보
     */
    @PostMapping("/signup")
    public ResponseEntity<SignupUserResponseDto> signUp(@Valid @RequestBody SignupUserRequestDto signupRequest){

        SignupUserResponseDto signUpResponseDto = usersService.signUp(signupRequest);

        return new ResponseEntity<>(signUpResponseDto, HttpStatus.CREATED);

    }

    /**
     * <p>로그인</p>
     *
     * @author 이현하
     * @param loginRequest 요청 DTO
     * { email, password }
     * @return LoginUserResponseDto 로그인한 유저 정보, token 생성된 인증 정보
     */
    @GetMapping("/login")
    public ResponseEntity<LoginUserResponseDto> logIn(@Valid @RequestBody LoginUserRequestDto loginRequest){

        LoginUserResponseDto loginResponseDto = usersService.logIn(loginRequest);

        // 유저 email 기준으로 토큰 부여
        String token = jwtUtil.createToken(loginResponseDto.getId(), loginResponseDto.getEmail(), UserRole.USER);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Authorization", token)
                .body(loginResponseDto);
    }

    /**
     * <p>유저 검색 (전체 검색, 이름 검색, 이메일 검색)</p>
     *
     * @author 이현하
     * @param name 검색할 유저 이름
     * @param email 검색할 이메일
     * @return List<SearchUserResponseDto> 검색된 유저 정보 리스트
     */
    @GetMapping("/search")
    public ResponseEntity<List<SearchUserResponseDto>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email
    ){
        List<SearchUserResponseDto> searchResponseList = usersService.search(name, email);

        return new ResponseEntity<>(searchResponseList, HttpStatus.OK);
    }


    @PutMapping("/modify")
    public ResponseEntity<String> modifyUserProfile(@Valid @RequestBody UpdateUserProfileRequestDto updateRequest){
        usersService.updateUserProfile(updateRequest);
        return ResponseEntity.ok("success");
    }


    @DeleteMapping("/quit")
    public ResponseEntity<String> quitUser(@Valid @RequestBody DeleteUserRequestDto deleteRequest){
        usersService.deleteUser(deleteRequest);
        return ResponseEntity.ok("success");
    }
}
