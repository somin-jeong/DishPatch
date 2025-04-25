package com.example.dishpatch.api.review.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dishpatch.api.review.request.ReviewCreateRequest;
import com.example.dishpatch.api.review.request.ReviewUpdateRequest;
import com.example.dishpatch.api.review.response.ReviewPageResponse;
import com.example.dishpatch.api.review.response.ReviewResponse;
import com.example.dishpatch.domain.review.service.ReviewService;
import com.example.dishpatch.global.security.UserAuth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;

	@PostMapping
	public ResponseEntity<ReviewResponse> createReview(
		@Valid @RequestBody ReviewCreateRequest request,
		@AuthenticationPrincipal UserAuth userAuth
	) {
		ReviewResponse response = reviewService.createReview(request, userAuth);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping
	public ResponseEntity<ReviewPageResponse> findReviews(
		@RequestParam Long storeId,
		@RequestParam Integer min,
		@RequestParam Integer max,
		@AuthenticationPrincipal UserAuth userAuth,
		@RequestParam(required = false, defaultValue = "0", value = "page") int page,
		@RequestParam(required = false, defaultValue = "10", value = "size") int size
	) {
		ReviewPageResponse responsePage = reviewService.findReviews(storeId, min, max, userAuth, page, size);

		return ResponseEntity.status(HttpStatus.OK).body(responsePage);
	}

	@PatchMapping("/reviews/{reviewId}")
	public ResponseEntity<ReviewResponse> updateReview(
		@PathVariable Long reviewId,
		@Valid @RequestBody ReviewUpdateRequest request,
		@AuthenticationPrincipal UserAuth userAuth
	) {
		ReviewResponse response = reviewService.updateReview(reviewId, request, userAuth);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@DeleteMapping("/reviews/{reviewId}")
	public ResponseEntity<Void> deleteReview(
		@PathVariable Long reviewId,
		@AuthenticationPrincipal UserAuth userAuth
	) {
		reviewService.deleteReview(reviewId, userAuth);

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
