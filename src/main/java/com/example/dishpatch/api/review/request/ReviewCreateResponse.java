package com.example.dishpatch.api.review.request;

import com.example.dishpatch.infra.db.review.entity.Review;
import com.example.dishpatch.infra.db.review.entity.ReviewStatus;

public record ReviewCreateResponse(
	Long id,
	Long menuId,
	Integer rating,
	String contents,
	String imageUrl,
	ReviewStatus status
) {

	public static ReviewCreateResponse from(Review review) {
		return new ReviewCreateResponse(
			review.getId(),
			review.getMenu().getId(), // 메뉴에 getId() 꼭 있어야 해!
			review.getRating(),
			review.getContents(),
			review.getImageUrl(),
			review.getStatus()
		);
	}
}
