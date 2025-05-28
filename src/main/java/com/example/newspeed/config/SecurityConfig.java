package com.example.newspeed.config;

import com.example.newspeed.Filter.JwtFilter;
import com.example.newspeed.constant.Const;
import com.example.newspeed.enums.UserRole;
import com.example.newspeed.exception.authentication.JwtAccessDeniedHandler;
import com.example.newspeed.exception.authentication.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session
                        -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests( auth -> auth
                        // 댓글 인가
                        .requestMatchers(HttpMethod.GET, Const.COMMENT_URL).permitAll()
                        .requestMatchers(HttpMethod.POST, Const.COMMENT_URL).hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, Const.COMMENT_URL).hasRole("USER")
                        .requestMatchers(HttpMethod.PATCH, Const.COMMENT_URL).hasRole(UserRole.USER.name())
                )
                .authorizeHttpRequests(auth -> auth
                        // 유저 인가
                        .requestMatchers(HttpMethod.POST, "/news-peed/users/signup").permitAll() // 회원가입: 모두 허용
                        .requestMatchers(HttpMethod.GET, "/news-peed/users/login").permitAll()   // 로그인: 모두 허용
                        .requestMatchers(HttpMethod.GET, "/news-peed/users/search").hasRole("USER") // 유저 검색: USER만
                        .requestMatchers(HttpMethod.PUT, "/news-peed/users/modify-name").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/news-peed/users/modify-password").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/news-peed/users/modify-intro").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/news-peed/users/modify-image").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/news-peed/users/quit").hasRole("USER")
                )
                .authorizeHttpRequests(auth -> auth
                        // 게시글 인가
                        .anyRequest().permitAll()
                )
                .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling( configure -> configure
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                )
                .build();
    }
}
