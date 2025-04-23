package com.example.dishpatch.api.store.response;

import com.example.dishpatch.infra.db.store.entity.Store;

public record StoreCreateResponse(
	Long storeId
) {
	public static StoreCreateResponse from(Store store) {
		return new StoreCreateResponse(store.getId());
	}
}
