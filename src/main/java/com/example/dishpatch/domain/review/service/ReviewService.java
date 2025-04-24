package com.example.dishpatch.domain.review.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.dishpatch.api.review.request.ReviewCreateRequest;
import com.example.dishpatch.api.review.request.ReviewUpdateRequest;
import com.example.dishpatch.api.review.response.ReviewPageResponse;
import com.example.dishpatch.api.review.response.ReviewResponse;
import com.example.dishpatch.domain.review.exception.ReviewErrorCode;
import com.example.dishpatch.domain.store.exception.StoreErrorCode;
import com.example.dishpatch.global.exception.BizException;
import com.example.dishpatch.infra.db.menu.entity.Menu;
import com.example.dishpatch.infra.db.menu.repository.MenuRepository;
import com.example.dishpatch.infra.db.review.entity.Review;
import com.example.dishpatch.infra.db.review.repository.ReviewRepository;
import com.example.dishpatch.infra.db.store.entity.Store;
import com.example.dishpatch.infra.db.store.repository.StoreRepository;
import com.example.dishpatch.infra.db.user.entity.User;
import com.example.dishpatch.infra.db.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final UserRepository userRepository;
	private final StoreRepository storeRepository;
	private final MenuRepository menuRepository;

	public ReviewResponse createReview(Long storeId, ReviewCreateRequest request) {
		Long userId = 1L;
		storeId = 1L;

		//userId 재설정 해야함
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

		//storeId 타입 맞춰야 함
		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new IllegalArgumentException("해당 가게를 찾을 수 없습니다."));

		Menu menu = menuRepository.findById(request.menuId())
			.orElseThrow(() -> new IllegalArgumentException("해당 메뉴를 찾을 수 없습니다."));

		Review review = new Review(user, store, menu, request.rating(), request.contents(), request.imageUrl(),
			request.status());
		Review saved = reviewRepository.save(review);

		return ReviewResponse.from(saved);
	}

	public ReviewPageResponse findReviews(Long storeId, Integer min, Integer max, int page, int size) {
		Integer safeMin = (min != null) ? min : 1;
		Integer safeMax = (max != null) ? max : 5;

		Long userId = 1L;

		//userId 재설정 해야함
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new BizException(StoreErrorCode.STORE_NOT_FOUND));

		Pageable pageable = PageRequest.of(page, size);

		Page<Review> reviewPage = reviewRepository.findAllByStoreIdAndRating(
			userId, store.getId(), safeMin, safeMax, pageable);

		return ReviewPageResponse.from(reviewPage);
	}

	public ReviewResponse updateReview(Long reviewId, ReviewUpdateRequest request) {
		Long userId = 1L;
		reviewId = 1L;

		//userId 재설정 해야함
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

		Review review = reviewRepository.findById(reviewId)
			.orElseThrow(() -> new BizException(ReviewErrorCode.REVIEW_NOT_FOUND));

		review.update(request.rating(), request.contents(), request.imageUrl(), request.status());

		return ReviewResponse.from(review);
	}

}
