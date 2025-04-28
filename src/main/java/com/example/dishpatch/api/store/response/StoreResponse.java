package com.example.dishpatch.api.store.response;

import java.io.Serializable;

import com.example.dishpatch.global.response.pagination.CursorSupport;
import com.example.dishpatch.infra.db.store.entity.Store;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreResponse implements CursorSupport, Serializable {
	Long id;
	String name;
	String imageUrl;
	int deliveryFee;
	int minDeliveryPrice;
	double rating;
	int reviewCount;

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

	@Override
	public Long getId() {
		return id;
	}

}
