package com.example.dishpatch.domain.review.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.dishpatch.api.review.request.ReviewCreateRequest;
import com.example.dishpatch.api.review.request.ReviewResponse;
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

	public List<ReviewResponse> findReviews(Long storeId, Integer min, Integer max) {
		Long userId = 1L;
		Integer safeMin = (min != null) ? min : 1;
		Integer safeMax = (max != null) ? max : 5;

		//userId 재설정 해야함
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new IllegalArgumentException("해당 가게를 찾을 수 없습니다."));

		List<Review> reviewList = reviewRepository.findAllByStoreIdAndRating(store.getId(), safeMin, safeMax);

		return ReviewResponse.from(reviewList);
	}
}
