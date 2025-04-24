package com.example.dishpatch.global.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.dishpatch.infra.db.user.repository.RedisRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final RedisRepository redisRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String header = request.getHeader("Authorization");

		if (header != null && header.startsWith("Bearer ")) {
			String token = header.substring(7);

			if (redisRepository.validateKey(token)) {
				return;
			}

			// JWT 유효성 검증
			try {
				if (jwtUtil.validateToken(token)) {
					// UserRole 검증
					UserAuth userAuth = jwtUtil.extractUserAuth(token);

					List<SimpleGrantedAuthority> authorities = List.of(
						// userAuth.getRole().name()의 .name()은 이넘타입인 UserRole을 String으로 가져오는 메서드다.
						new SimpleGrantedAuthority("ROLE_" + userAuth.getRole().name())
					);

					UsernamePasswordAuthenticationToken authToken =
						new UsernamePasswordAuthenticationToken(userAuth, null, authorities);

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
