package com.example.dishpatch.api.review.request;

import com.example.dishpatch.infra.db.review.entity.ReviewStatus;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ReviewCreateRequest(

	Long orderId,

	Long menuId,

	@NotNull
	@Min(1)
	@Max(5)
	Integer rating,

	@Max(255)
	String contents,

	@Max(255)
	String imageUrl,

	@NotNull
	ReviewStatus status
) {
}
