package com.example.newspeed.config;

import com.example.newspeed.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

/**
 * JWT 토큰의 생성, 검증, 파싱을 담당하는 유틸리티 클래스
 * 토큰 생성, 검증, 사용자 정보 추출 등의 기능을 제공합니다.
 *
 * @author 이준영
 */
@Slf4j(topic = "JwtUtil")
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtProperties jwtProperties;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    /**
     * JWT 시크릿 키를 초기화합니다.
     * 애플리케이션 시작 시 자동으로 호출됩니다.
     *
     * @throws ResponseStatusException JWT 설정이 잘못된 경우
     */
    @PostConstruct
    public void init() {
        try {
            String secretKey = jwtProperties.getSecretKey();
            if (!StringUtils.hasText(secretKey)) {
                throw new IllegalArgumentException("JWT 시크릿 키가 설정되지 않았습니다.");
            }
            
            byte[] bytes = Base64.getDecoder().decode(secretKey);
            if (bytes.length < 32) {
                throw new IllegalArgumentException("JWT 시크릿 키는 최소 32바이트 이상이어야 합니다.");
            }
            
            key = Keys.hmacShaKeyFor(bytes);
        } catch (IllegalArgumentException e) {
            log.error("JWT 시크릿 키 초기화 실패: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "JWT 설정 오류");
        }
    }

    /**
     * 새로운 JWT 토큰을 생성합니다.
     *
     * @param userId 사용자 ID
     * @param email 사용자 이메일
     * @param userRole 사용자 권한
     * @return 생성된 JWT 토큰
     */
    public String createToken(Long userId, String email, UserRole userRole) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getTokenValidityInMilliseconds());

        return jwtProperties.getBearerPrefix() +
                Jwts.builder()
                        .setSubject(String.valueOf(userId))
                        .claim("email", email)
                        .claim("userRole", userRole)
                        .setExpiration(expiryDate)
                        .setIssuedAt(now)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    /**
     * Bearer 토큰에서 실제 토큰 값을 추출합니다.
     *
     * @param tokenValue Bearer 토큰
     * @return 추출된 토큰 값
     * @throws ResponseStatusException 토큰이 없거나 형식이 잘못된 경우
     */
    public String substringToken(String tokenValue) {
        if (!StringUtils.hasText(tokenValue)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "토큰이 존재하지 않습니다.");
        }
        if (!tokenValue.startsWith(jwtProperties.getBearerPrefix())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "잘못된 토큰 형식입니다.");
        }
        return tokenValue.substring(jwtProperties.getBearerPrefix().length());
    }

    /**
     * JWT 토큰에서 클레임을 추출합니다.
     *
     * @param token JWT 토큰
     * @return 추출된 클레임
     * @throws ResponseStatusException 토큰이 만료되었거나 잘못된 경우
     */
    public Claims extractClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다.");
        } catch (SecurityException | MalformedJwtException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "잘못된 JWT 서명입니다.");
        } catch (UnsupportedJwtException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT 토큰이 잘못되었습니다.");
        }
    }

    /**
     * JWT 토큰에서 사용자 ID를 추출합니다.
     *
     * @param token JWT 토큰
     * @return 사용자 ID
     * @throws ResponseStatusException 사용자 ID 형식이 잘못된 경우
     */
    public Long getUserId(String token) {
        try {
            String userId = extractClaims(token).getSubject();
            return Long.parseLong(userId);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "잘못된 사용자 ID 형식입니다.");
        }
    }

    /**
     * JWT 토큰에서 사용자 권한을 추출합니다.
     *
     * @param token JWT 토큰
     * @return 사용자 권한
     * @throws ResponseStatusException 사용자 권한이 잘못된 경우
     */
    public UserRole getUserRole(String token) {
        try {
            String userRole = extractClaims(token).get("userRole", String.class);
            return UserRole.of(userRole);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "잘못된 사용자 권한입니다.");
        }
    }

    /**
     * JWT 토큰에서 사용자 이메일을 추출합니다.
     *
     * @param token JWT 토큰
     * @return 사용자 이메일
     * @throws ResponseStatusException 이메일 정보가 없는 경우
     */
    public String getEmail(String token) {
        String userEmail = extractClaims(token).get("email", String.class);
        if (userEmail == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "이메일 정보가 없습니다.");
        }
        return userEmail;
    }
}