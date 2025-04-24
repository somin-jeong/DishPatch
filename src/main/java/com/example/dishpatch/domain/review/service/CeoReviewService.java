package com.example.dishpatch.domain.review.service;

import java.util.List;

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

		//사장님 댓글(자식) 생성
		CeoReview ceoReview = new CeoReview(user, request.contents(), request.status(), review);

		//연관관계 수동 설정 (부모->자식)
		//save 호출 전 연관된 객체들 모두 영속성 컨텍스트에 등록
		review.getCeoReviews().add(ceoReview);

		//DB 저장
		//외래키 반영
		CeoReview saved = ceoReviewRepository.save(ceoReview);

		return CeoReviewResponse.from(List.of(ceoReview)).get(0);
	}
}
