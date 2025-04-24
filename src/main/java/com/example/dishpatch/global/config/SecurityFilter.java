package com.example.dishpatch.global.config;

import java.io.IOException;
import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final RedisTemplate<String, String> redisTemplate;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String header = request.getHeader("Authorization");

		if (header != null && header.startsWith("Bearer ")) {
			String token = header.substring(7);

			// Redis 블랙리스트 검사
			String blacklistKey = "BlackList:" + token;
			// haskey()는 Boolean 객체를 반환하기 때문에 그냥 ==ture 또는 isTure()로 비교하면 NPE 발생가능성 존재
			if (Boolean.TRUE.equals(redisTemplate.hasKey(blacklistKey))) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "이미 로그아웃된 토큰입니다.");
				return;
			}

			// JWT 유효성 검증
			try {
				if (jwtUtil.validateToken(token)) {
					Long id = jwtUtil.extractId(token);

					UsernamePasswordAuthenticationToken authToken =
						new UsernamePasswordAuthenticationToken(id, null, List.of());

					SecurityContextHolder.getContext().setAuthentication(authToken);

				}
			} catch (Exception e) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 접근입니다.");
				return;
			}
		}

		filterChain.doFilter(request, response);
	}
}
