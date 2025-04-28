package com.example.dishpatch.api.store.response;

import com.example.dishpatch.infra.db.store.entity.Store;

public record StoreInfoResponse(
	String name,
	String address,
	String imageUrl,
	String phone,
	int deliveryFee,
	String introduction,
	int minDeliveryPrice,
	double rating,
	int reviewCount,
	String openTime,
	String closeTime
) {
	public static StoreInfoResponse from(Store store) {
		return new StoreInfoResponse(
			store.getName(),
			store.getAddress(),
			store.getImage(),
			store.getPhone(),
			store.getDeliveryFee(),
			store.getIntroduction(),
			store.getMinDeliveryPrice(),
			store.getRating(),
			store.getReviewCount(),
			String.valueOf(store.getOpenTime()),
			String.valueOf(store.getCloseTime())
		);
	}
}
