package com.example.newspeed.config;

import com.example.newspeed.Filter.JwtFilter;
import com.example.newspeed.Filter.JwtLoginBlockFilter;
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

/**
 * <p>스프링 시큐리티</p>
 *
 * @author 이준영
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtLoginBlockFilter jwtLoginBlockFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> 
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 댓글 인가
                        .requestMatchers(HttpMethod.GET, Const.COMMENT_URL).permitAll()
                        .requestMatchers(HttpMethod.POST, Const.COMMENT_URL).hasRole(UserRole.USER.name())
                        .requestMatchers(HttpMethod.DELETE, Const.COMMENT_URL).hasRole(UserRole.USER.name())
                        .requestMatchers(HttpMethod.PATCH, Const.COMMENT_URL).hasRole(UserRole.USER.name())
                        
                        // 유저 인가
                        .requestMatchers(HttpMethod.POST, Const.USER_URL + "/signup").permitAll()
                        .requestMatchers(HttpMethod.GET, Const.USER_URL + "/login").permitAll()
                        .requestMatchers(HttpMethod.GET, Const.USER_URL + "/search").hasRole(UserRole.USER.name())
                        .requestMatchers(HttpMethod.PUT, Const.USER_URL + "/modify").hasRole(UserRole.USER.name())
                        .requestMatchers(HttpMethod.DELETE, Const.USER_URL + "/quit").hasRole(UserRole.USER.name())
                        
                        // 팔로우 인가
                        .requestMatchers(HttpMethod.POST, Const.FOLLOWS_URL + "/follow").hasRole(UserRole.USER.name())
                        .requestMatchers(HttpMethod.DELETE, Const.FOLLOWS_URL + "/unfollow").hasRole(UserRole.USER.name())
                        
                        // 게시글 인가
                        .requestMatchers(HttpMethod.PATCH, Const.POST_URL + "/like").hasRole(UserRole.USER.name())
                        .requestMatchers(HttpMethod.POST, Const.POST_URL + "/create-posts").hasRole(UserRole.USER.name())
                        .requestMatchers(HttpMethod.DELETE, Const.POST_URL + "/delete-posts").hasRole(UserRole.USER.name())
                        .requestMatchers(HttpMethod.PATCH, Const.POST_URL + "/update-posts").hasRole(UserRole.USER.name())
                        .anyRequest().permitAll()
                )
                .addFilterBefore(jwtLoginBlockFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(configure -> configure
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                )
                .build();
    }
}
