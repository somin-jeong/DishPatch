package com.example.dishpatch.global.config;

import java.util.Date;

import javax.crypto.SecretKey;

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

	private final long expiration = 30 * 60 * 1000L;

	/**
	 * JWT 토큰 생성
	 * deprecated 예정인 메서드라고함
	 */
	SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());

	public String createToken(Long id) {
		return Jwts.builder()
			.setSubject(String.valueOf(id))
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + expiration))
			.signWith(key)
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
