package com.example.dishpatch.domain.review.service;

import org.springframework.stereotype.Service;

import com.example.dishpatch.api.review.request.CeoReviewCreateRequest;
import com.example.dishpatch.api.review.response.CeoReviewResponse;
import com.example.dishpatch.domain.review.exception.ReviewErrorCode;
import com.example.dishpatch.global.exception.BizException;
import com.example.dishpatch.infra.db.review.entity.CeoReview;
import com.example.dishpatch.infra.db.review.entity.Review;
import com.example.dishpatch.infra.db.review.repository.CeoReviewRepository;
import com.example.dishpatch.infra.db.review.repository.ReviewRepository;
import com.example.dishpatch.infra.db.user.entity.User;
import com.example.dishpatch.infra.db.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CeoReviewService {

	private final UserRepository userRepository;
	private final ReviewRepository reviewRepository;
	private final CeoReviewRepository ceoReviewRepository;

	public CeoReviewResponse createCeoReview(Long reviewId, CeoReviewCreateRequest request) {
		long userId = 1L;

		Review review = reviewRepository.findById(reviewId)
			.orElseThrow(() -> new BizException(ReviewErrorCode.REVIEW_NOT_FOUND));

		//userId 재설정 해야함
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

		CeoReview ceoReview = new CeoReview(user, request.contents(), request.status());
		CeoReview saved = ceoReviewRepository.save(ceoReview);

		return CeoReviewResponse.from(saved);
	}
}
