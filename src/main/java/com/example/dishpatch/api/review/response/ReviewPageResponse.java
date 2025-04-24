package com.example.dishpatch.api.review.response;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import com.example.dishpatch.infra.db.review.entity.Review;

public record ReviewPageResponse(
	List<ReviewResponse> content,
	int page,
	int size,
	int totalPages,
	long totalElements
) {
	public static ReviewPageResponse from(Page<Review> page) {
		List<ReviewResponse> content = page.getContent().stream()
			.map(ReviewResponse::from)
			.collect(Collectors.toList());

		return new ReviewPageResponse(
			content,
			page.getNumber(),
			page.getSize(),
			page.getTotalPages(),
			page.getTotalElements()
		);
	}
}
