package com.example.dishpatch.infra.db.store.repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.dishpatch.api.store.response.KeywordRank;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class KeywordRedisRepository {
	private final StringRedisTemplate redisTemplate;
	private static final String POPULAR_KEYWORDS_KEY = "popular:keywords:"; // Redis에 저장할 키
	private static final Duration TTL = Duration.ofHours(2); // 2시간 뒤 만료

	public void increaseKeywordScore(String keyword) {
		if (keyword == null || keyword.isBlank()) {
			return;
		}
		keyword = keyword.replace(" ", "");

		String key = generateCurrentHourKey();

		redisTemplate.opsForZSet().incrementScore(key, keyword, 1);
		redisTemplate.expire(POPULAR_KEYWORDS_KEY, TTL);
	}

	@Transactional(readOnly = true)
	public List<KeywordRank> getPopularKeywords(int size) {
		List<KeywordRank> keywordRanks = new ArrayList<>();
		String key = generatePreviousHourKey();

		Set<ZSetOperations.TypedTuple<String>> typedTuples = redisTemplate.opsForZSet()
			.reverseRangeWithScores(key, 0, size - 1);

		if (typedTuples == null || typedTuples.isEmpty()) {
			key = generateCurrentHourKey();
			typedTuples = redisTemplate.opsForZSet()
				.reverseRangeWithScores(key, 0, size - 1);
		}

		int rank = 1;
		for (ZSetOperations.TypedTuple<String> tuple : typedTuples) {
			if (tuple.getValue() == null || tuple.getScore() == null) {
				continue;
			}
			keywordRanks.add(new KeywordRank(
				rank++,
				tuple.getValue(),
				tuple.getScore().intValue()
			));
		}

		return keywordRanks;
	}

	private String generateCurrentHourKey() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHH");
		return POPULAR_KEYWORDS_KEY + now.format(formatter);
	}

	private String generatePreviousHourKey() {
		LocalDateTime now = LocalDateTime.now().minusHours(1);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHH");
		return POPULAR_KEYWORDS_KEY + now.format(formatter);
	}
}
