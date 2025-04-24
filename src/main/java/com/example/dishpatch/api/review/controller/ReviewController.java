package com.example.dishpatch.api.review.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

	@GetMapping("/reviews")
	public ResponseEntity<ReviewPageResponse> findReviews(
		@PathVariable Long storeId,
		@RequestParam Integer min,
		@RequestParam Integer max,
		@RequestParam(required = false, defaultValue = "0", value = "page") int page,
		@RequestParam(required = false, defaultValue = "10", value = "size") int size
		//@SessionAttribute("loginUser") Long loginUserId
	) {
		ReviewPageResponse responsePage = reviewService.findReviews(storeId, min, max, page, size);

		return ResponseEntity.status(HttpStatus.OK).body(responsePage);
	}

	@PatchMapping("/reviews/{reviewId}")
	public ResponseEntity<ReviewResponse> updateReview(
		@PathVariable Long reviewId,
		@Valid @RequestBody ReviewUpdateRequest request
		//@SessionAttribute("loginUser") Long loginUserId
	) {
		ReviewResponse response = reviewService.updateReview(reviewId, request);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@DeleteMapping("/reviews/{reviewId}")
	public ResponseEntity<Void> deleteReview(
		@PathVariable Long reviewId
		//@SessionAttribute("loginUser") Long loginUserId
	) {
		reviewService.deleteReview(reviewId);

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
