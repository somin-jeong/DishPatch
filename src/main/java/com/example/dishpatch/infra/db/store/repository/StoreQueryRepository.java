package com.example.dishpatch.infra.db.store.repository;

import org.springframework.data.domain.Slice;

import com.example.dishpatch.api.store.response.StoreResponse;
import com.example.dishpatch.infra.db.store.enums.SortType;

public interface StoreQueryRepository {
	Slice<StoreResponse> findAllByCategoryId(SortType sortType, Long categoryId, Long cursorId, int size);
}
