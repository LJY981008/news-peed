package com.example.newspeed.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT 토큰 관련 설정 속성을 관리하는 클래스
 * application.properties에서 'jwt' 접두사로 시작하는 속성들을 매핑합니다.
 *
 * @author 이준영
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    /**
     * JWT 토큰 서명에 사용되는 비밀 키
     */
    private String secretKey;

    /**
     * JWT 토큰의 유효 기간 (밀리초 단위)
     */
    private long tokenValidityInMilliseconds;

    /**
     * JWT 토큰의 Bearer 접두사
     */
    private String bearerPrefix;
} 