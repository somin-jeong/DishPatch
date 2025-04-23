package com.example.dishpatch.api.review.request;

import com.example.dishpatch.infra.db.review.entity.ReviewStatus;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReviewCreateRequest {

	private Long menuId;

	@NotNull
	private int rating;

	@Max(255)
	private String contents;

	@Max(255)
	private String imageUrl;

	@NotNull
	private ReviewStatus status;

}
