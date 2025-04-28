package com.example.dishpatch.api.store.response;

import java.util.List;

import com.example.dishpatch.global.response.pagination.CursorSupport;

import lombok.Getter;

@Getter
public class StoreSearchResponse implements CursorSupport {
	Long id;
	String name;
	String imageUrl;
	int deliveryFee;
	int minDeliveryPrice;
	double rating;
	int reviewCount;
	List<String> menuNameList;

	public StoreSearchResponse(Long id, String name, String imageUrl, int deliveryFee, int minDeliveryPrice,
		double rating, int reviewCount) {
		this.id = id;
		this.name = name;
		this.imageUrl = imageUrl;
		this.deliveryFee = deliveryFee;
		this.minDeliveryPrice = minDeliveryPrice;
		this.rating = rating;
		this.reviewCount = reviewCount;
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setMenuNameList(List<String> menuNameList) {
		this.menuNameList = (menuNameList != null) ? menuNameList : List.of();
	}
}
