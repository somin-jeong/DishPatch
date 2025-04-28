package com.example.dishpatch.api.review.request;

import com.example.dishpatch.infra.db.review.entity.ReviewStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CeoReviewCreateRequest(

	@Size(max = 255)
	String contents,

	@NotNull
	ReviewStatus status
) {
}
