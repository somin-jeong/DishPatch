package com.example.dishpatch.domain.review.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.dishpatch.api.review.request.ReviewCreateRequest;
import com.example.dishpatch.api.review.request.ReviewUpdateRequest;
import com.example.dishpatch.api.review.response.ReviewPageResponse;
import com.example.dishpatch.api.review.response.ReviewResponse;
import com.example.dishpatch.domain.menu.exception.MenuErrorCode;
import com.example.dishpatch.domain.order.exception.OrderErrorCode;
import com.example.dishpatch.domain.review.exception.ReviewErrorCode;
import com.example.dishpatch.domain.store.exception.StoreErrorCode;
import com.example.dishpatch.domain.user.exception.UserErrorCode;
import com.example.dishpatch.global.exception.BizException;
import com.example.dishpatch.global.security.UserAuth;
import com.example.dishpatch.infra.db.menu.entity.Menu;
import com.example.dishpatch.infra.db.menu.repository.MenuRepository;
import com.example.dishpatch.infra.db.order.entity.Order;
import com.example.dishpatch.infra.db.order.entity.OrderStatus;
import com.example.dishpatch.infra.db.order.repository.OrderRepository;
import com.example.dishpatch.infra.db.review.entity.Review;
import com.example.dishpatch.infra.db.review.repository.ReviewRepository;
import com.example.dishpatch.infra.db.store.entity.Store;
import com.example.dishpatch.infra.db.store.repository.StoreRepository;
import com.example.dishpatch.infra.db.user.entity.User;
import com.example.dishpatch.infra.db.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final UserRepository userRepository;
	private final OrderRepository orderRepository;
	private final StoreRepository storeRepository;
	private final MenuRepository menuRepository;

	public ReviewResponse createReview(ReviewCreateRequest request, UserAuth userAuth) {
		User user = userRepository.findById(userAuth.getId())
			.orElseThrow(() -> new BizException(UserErrorCode.INVALID_ID));

		Order order = orderRepository.findById(request.orderId())
			.orElseThrow(() -> new BizException(OrderErrorCode.ORDER_NOT_FOUND));

		Store store = storeRepository.findById(order.getStore().getId())
			.orElseThrow(() -> new BizException(StoreErrorCode.STORE_NOT_FOUND));

		Menu menu = menuRepository.findById(request.menuId())
			.orElseThrow(() -> new BizException(MenuErrorCode.MENU_NOT_FOUND));

		if (!order.getStatus().equals(OrderStatus.FINISHED)) {
			throw new BizException(ReviewErrorCode.REVIEW_NOT_ALLOWED_BEFORE_DELIVERY);
		}

		Review review = new Review(user, store, menu, order, request.rating(), request.contents(), request.imageUrl(),
			request.status());
		Review saved = reviewRepository.save(review);

		return ReviewResponse.from(saved);
	}

	public ReviewPageResponse findReviews(Long storeId, Integer min, Integer max, UserAuth userAuth, int page,
		int size) {
		Integer safeMin = (min != null) ? min : 1;
		Integer safeMax = (max != null) ? max : 5;

		User user = userRepository.findById(userAuth.getId())
			.orElseThrow(() -> new BizException(UserErrorCode.INVALID_ID));

		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new BizException(StoreErrorCode.STORE_NOT_FOUND));

		Pageable pageable = PageRequest.of(page, size);

		Page<Review> reviewPage = reviewRepository.findAllByStoreIdAndRating(
			user.getId(), store.getId(), safeMin, safeMax, pageable);

		return ReviewPageResponse.from(reviewPage);
	}

	@Transactional
	public ReviewResponse updateReview(Long reviewId, ReviewUpdateRequest request, UserAuth userAuth) {
		User user = userRepository.findById(userAuth.getId())
			.orElseThrow(() -> new BizException(UserErrorCode.INVALID_ID));

		Review review = reviewRepository.findById(reviewId)
			.orElseThrow(() -> new BizException(ReviewErrorCode.REVIEW_NOT_FOUND));

		review.update(request.rating(), request.contents(), request.imageUrl(), request.status());

		return ReviewResponse.from(review);
	}

	@Transactional
	public void deleteReview(Long reviewId, UserAuth userAuth) {
		Review review = reviewRepository.findById(reviewId)
			.orElseThrow(() -> new BizException(ReviewErrorCode.REVIEW_NOT_FOUND));

		if (!userAuth.getId().equals(review.getUser().getId())) {
			throw new BizException(ReviewErrorCode.REVIEW_AUTHOR_MISMATCH);
		}

		reviewRepository.deleteById(review.getId());
	}

}
