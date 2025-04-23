package com.example.dishpatch.api.review.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dishpatch.api.review.request.ReviewCreateRequest;
import com.example.dishpatch.api.review.request.ReviewResponse;
import com.example.dishpatch.domain.review.service.ReviewService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/stores/{storeId}")
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;

	@PostMapping("/reviews")
	public ResponseEntity<ReviewResponse> createReview(
		@PathVariable Long storeId,
		@Valid @RequestBody ReviewCreateRequest request
		//@SessionAttribute("loginUser") Long loginUserId
	) {
		ReviewResponse response = reviewService.createReview(storeId, request);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
