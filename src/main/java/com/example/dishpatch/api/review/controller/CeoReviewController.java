package com.example.dishpatch.api.review.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dishpatch.api.review.request.CeoReviewCreateRequest;
import com.example.dishpatch.api.review.request.CeoReviewUpdateRequest;
import com.example.dishpatch.api.review.response.CeoReviewResponse;
import com.example.dishpatch.domain.review.service.CeoReviewService;
import com.example.dishpatch.global.security.UserAuth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/review/{reviewId}")
@RequiredArgsConstructor
public class CeoReviewController {

	private final CeoReviewService ceoReviewService;

	@PostMapping("/ceoreview")
	public ResponseEntity<CeoReviewResponse> createCeoReview(
		@PathVariable Long reviewId,
		@Valid @RequestBody CeoReviewCreateRequest request
	) {
		CeoReviewResponse response = ceoReviewService.createCeoReview(reviewId, request);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PatchMapping("/ceoreview/{ceoReviewId}")
	public ResponseEntity<CeoReviewResponse> updateCeoReview(
		@PathVariable Long reviewId,
		@PathVariable Long ceoReviewId,
		@Valid @RequestBody CeoReviewUpdateRequest request
	) {
		CeoReviewResponse response = ceoReviewService.updateCeoReview(reviewId, ceoReviewId, request);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@DeleteMapping("/ceoreview/{ceoReviewId}")
	public ResponseEntity<Void> deleteCeoReview(
		@PathVariable Long reviewId,
		@PathVariable Long ceoReviewId,
		@AuthenticationPrincipal UserAuth userAuth
	) {
		ceoReviewService.deleteCeoReview(reviewId, ceoReviewId, userAuth);

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
