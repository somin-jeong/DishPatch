package com.example.dishpatch.api.review.request;

import com.example.dishpatch.infra.db.review.entity.ReviewStatus;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;

public record CeoReviewCreateRequest(

	@Max(255)
	String contents,

	@NotNull
	ReviewStatus status
) {
}
