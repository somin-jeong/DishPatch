package com.example.dishpatch.infra.db.user.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.example.dishpatch.domain.user.exception.UserErrorCode;
import com.example.dishpatch.global.exception.BizException;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RedisRepository {

	private final RedisTemplate<String, String> redisTemplate;

	// haskey()는 Boolean 객체를 반환하기 때문에 그냥 ==ture 또는 isTure()로 비교하면 NPE 발생가능성 존재

	public boolean validateKey(String token) {
		// Redis 블랙리스트 검사
		String blacklistKey = "blackList:" + token;

		try {
			return redisTemplate.hasKey(blacklistKey);
		} catch (Exception e) {
			throw new BizException(UserErrorCode.INVALID_REQUEST);
		}
	}

}
