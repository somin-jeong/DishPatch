package com.example.dishpatch.global.config;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtUtil {

	@Value("${jwt.secret}")
	private String secretKey;

	private static final long expiration = 30 * 60 * 1000L;

	/**
	 * JWT 토큰 생성
	 * deprecated 예정인 메서드라고함
	 */
	public String createToken(Long id) {
		// SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
		return Jwts.builder()
			.setSubject(String.valueOf(id))
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + expiration))
			.signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
			.compact();
	}

	/**
	 * 토큰에서 아이디 추출
	 */
	public Long extractId(String token) {
		return Long.parseLong(Jwts.parserBuilder()
			.setSigningKey(secretKey.getBytes())
			.build()
			.parseClaimsJws(token)
			.getBody()
			.getSubject());
	}

	/**
	 * 토큰 유효성 검증
	 */
	public boolean validateToken(String token) {
		try {
			extractId(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// 헤더에서 토큰 추출
	public String extractToken(HttpServletRequest request) {
		String bearer = request.getHeader("Authorization");
		if (bearer != null && bearer.startsWith("Bearer ")) {
			return bearer.substring(7);
		}
		return null;
	}

	// 만료시간 추출
	public long getExpiration(String token) {
		Claims claims = Jwts.parserBuilder()
			.setSigningKey(secretKey.getBytes())
			.build()
			.parseClaimsJws(token)
			.getBody();
		return claims.getExpiration().getTime() - System.currentTimeMillis();
	}

}
