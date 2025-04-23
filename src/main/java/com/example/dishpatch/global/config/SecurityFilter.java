package com.example.dishpatch.global.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {

            String token = header.substring(7);

            try {
                if (jwtUtil.validateToken(token)) {
                    String email = jwtUtil.extractEmail(token);

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(email, null, List.of());

                    SecurityContextHolder.getContext().setAuthentication(authToken);

                }
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 접근입니다.");
            }
        }

        filterChain.doFilter(request, response);
    }
}
