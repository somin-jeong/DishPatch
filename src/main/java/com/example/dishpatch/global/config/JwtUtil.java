package com.example.dishpatch.global.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private static final String SECRETE_KEY = "dishpatchjwttwjhctaphsidpatchdishjwtjwtpatchdish";
    private final long expiration = 30 * 60 * 1000L;

    /**
     * JWT 토큰 생성
     */
    public String createToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(Keys.hmacShaKeyFor(SECRETE_KEY.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 토큰에서 이메일 추출
     */
    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRETE_KEY.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * 토큰 유효성 검증
     */
    public boolean validateToken(String token) {
        try {
            extractEmail(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
