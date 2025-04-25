package com.example.dishpatch.infra.db.store.repository;

import org.springframework.data.domain.Slice;

import com.example.dishpatch.api.store.response.StoreResponse;

public interface StoreQueryRepository {
	Slice<StoreResponse> findAllByCategoryId(Long categoryId, Long cursorId, int size);
}
