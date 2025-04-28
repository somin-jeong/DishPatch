package com.example.dishpatch.api.store.response;

import java.util.List;

public record PopularKeywordsResponse(
	List<KeywordRank> keywordRanks
) {
	public static PopularKeywordsResponse of(List<KeywordRank> keywordRanks) {
		return new PopularKeywordsResponse(keywordRanks);
	}
}
