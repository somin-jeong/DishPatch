package com.example.dishpatch.api.store.response;

public record KeywordRank(
	int rank,
	String keyword,
	int count
) {
}
