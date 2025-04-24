package com.example.dishpatch.api.review.response;

import java.util.List;

import com.example.dishpatch.infra.db.review.entity.CeoReview;
import com.example.dishpatch.infra.db.review.entity.ReviewStatus;

public record CeoReviewResponse(
	Long id,
	String contents,
	ReviewStatus status
) {
	public static CeoReviewResponse from(CeoReview ceoReview) {
		return new CeoReviewResponse(
			ceoReview.getId(),
			ceoReview.getContents(),
			ceoReview.getStatus()
		);
	}

	public static List<CeoReviewResponse> from(List<CeoReview> ceoReviewList) {
		return ceoReviewList.stream().map(CeoReviewResponse::from).toList();
	}
}
