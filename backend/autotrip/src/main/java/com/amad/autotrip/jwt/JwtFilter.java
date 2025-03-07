package com.amad.autotrip.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        //Header 검증
        if (authHeader != null || authHeader.startsWith("Bearer ")) {
            System.out.println("token null!!!!!");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.split(" ")[1];

        //token 소멸 시간 검증
        if (jwtUtil.isExpired(token)) {
            System.out.println("token expired!!!");
            filterChain.doFilter(request, response);
            return;
        }


    }
}
