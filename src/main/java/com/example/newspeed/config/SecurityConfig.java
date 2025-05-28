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

    //TODO authorizeHttpRequests 롤 설정 필요
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session
                        -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests( auth -> auth
                        .requestMatchers(HttpMethod.GET, Const.COMMENT_URL).permitAll()
                        .requestMatchers(HttpMethod.POST, Const.COMMENT_URL).hasRole(UserRole.USER.name())
                        .requestMatchers(HttpMethod.DELETE, Const.COMMENT_URL).hasRole(UserRole.USER.name())
                        .requestMatchers(HttpMethod.PATCH, Const.COMMENT_URL).hasRole(UserRole.USER.name())
                        .anyRequest().permitAll()
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/news-peed/logout").authenticated()
                )
                .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling( configure -> configure
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                )
                .build();
    }
}
