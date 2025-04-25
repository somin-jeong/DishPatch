package com.example.dishpatch.api.store.response;

import com.example.dishpatch.infra.db.store.entity.Store;

public record StoreResponse(
	Long id,
	String name,
	String imageUrl,
	int deliveryFee,
	int minDeliveryPrice,
	double rating,
	int reviewCount
) {
	public static StoreResponse from(Store store) {
		return new StoreResponse(
			store.getId(),
			store.getName(),
			store.getImage(),
			store.getDeliveryFee(),
			store.getMinDeliveryPrice(),
			store.getRating(),
			store.getReviewCount()
		);
	}
}
