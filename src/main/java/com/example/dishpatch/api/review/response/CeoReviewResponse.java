package com.example.dishpatch.api.review.response;

import java.util.List;
import java.util.stream.Collectors;

import com.example.dishpatch.infra.db.review.entity.CeoReview;
import com.example.dishpatch.infra.db.review.entity.ReviewStatus;

public record CeoReviewResponse(
	Long id,
	Long reviewId,
	Long userId,
	String contents,
	ReviewStatus status
) {
	public static List<CeoReviewResponse> from(List<CeoReview> ceoReviews) {
		return ceoReviews.stream()
			.map(ceoReview -> new CeoReviewResponse(
				ceoReview.getId(),
				ceoReview.getReview().getId(),
				ceoReview.getUser().getId(),
				ceoReview.getContents(),
				ceoReview.getStatus()
			))
			.collect(Collectors.toList());
	}

}
