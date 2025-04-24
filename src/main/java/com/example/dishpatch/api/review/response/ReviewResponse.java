package com.example.dishpatch.api.review.response;

import java.util.List;

import com.example.dishpatch.infra.db.review.entity.Review;
import com.example.dishpatch.infra.db.review.entity.ReviewStatus;

public record ReviewResponse(
	Long id,
	Long storeId,
	Long menuId,
	Integer rating,
	String contents,
	String imageUrl,
	ReviewStatus status,
	List<CeoReviewResponse> ceoReviewResponses
) {

	public static ReviewResponse from(Review review) {
		return new ReviewResponse(
			review.getId(),
			review.getStore().getId(),
			review.getMenu().getId(),
			review.getRating(),
			review.getContents(),
			review.getImageUrl(),
			review.getStatus(),
			CeoReviewResponse.from(review.getCeoReviews())
		);
	}
}
