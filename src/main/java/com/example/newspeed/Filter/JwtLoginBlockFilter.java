package com.example.newspeed.Filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 중복 로그인 방지 필터
 *
 * @author 이현하
 */

@Component
public class JwtLoginBlockFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {


        String path = request.getRequestURI();
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(authHeader == null){
            filterChain.doFilter(request, response);
            return;
        }

        if ("/news-peed/users/login".equals(path)
                && authHeader.startsWith("Bearer ")) {

            response.sendError(HttpServletResponse.SC_FORBIDDEN, "이미 로그인된 사용자입니다.");
            return;
        }

        filterChain.doFilter(request, response);

    }
}

