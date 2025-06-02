package com.example.newspeed.jwt;

import com.example.newspeed.config.JwtUtil;
import com.example.newspeed.dto.user.AuthUserDto;
import com.example.newspeed.enums.UserRole;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class JwtTokenProviderTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("JWT 토큰 생성 테스트")
    void createTokenTest(){
        //given
        AuthUserDto userDto = new AuthUserDto(1L, "test@test.com", UserRole.USER);

        //when
        String token = jwtUtil.createToken(userDto.getId(), userDto.getEmail(), userDto.getUserRole());

        //then
        assertNotNull(token);
    }

    @Test
    @DisplayName("JWT 토큰 정보 추출 테스트")
    void getTokenTest(){
        //given
        AuthUserDto userDto = new AuthUserDto(1L, "test@test.com", UserRole.USER);
        String token = jwtUtil.createToken(userDto.getId(), userDto.getEmail(), userDto.getUserRole());

        //when
        token = jwtUtil.substringToken(token);
        Long extractedUserId = jwtUtil.getUserId(token);
        String extractedUserEmail = jwtUtil.getEmail(token);
        UserRole extractedUserRole = jwtUtil.getUserRole(token);

        //then
        assertEquals(userDto.getId(), extractedUserId);
        assertEquals(userDto.getEmail(), extractedUserEmail);
        assertEquals(userDto.getUserRole(), extractedUserRole);
    }

    @Test
    @DisplayName("JWT 토큰 파싱 테스트")
    void parseTokenTest(){
        //given
        AuthUserDto userDto = new AuthUserDto(1L, "test@test.com", UserRole.USER);
        String token = jwtUtil.createToken(userDto.getId(), userDto.getEmail(), userDto.getUserRole());

        //when
        token = jwtUtil.substringToken(token);
        Claims claims = jwtUtil.extractClaims(token);

        //then
        assertNotNull(token);
        assertEquals(String.valueOf(userDto.getId()), claims.getSubject());
        assertEquals(userDto.getEmail(), claims.get("email", String.class));
        assertEquals(userDto.getUserRole().name(), claims.get("userRole", String.class));
    }
}
