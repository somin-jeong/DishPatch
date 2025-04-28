package com.example.dishpatch.domain.review.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.dishpatch.api.review.request.ReviewCreateRequest;
import com.example.dishpatch.api.review.request.ReviewUpdateRequest;
import com.example.dishpatch.api.review.response.ReviewPageResponse;
import com.example.dishpatch.api.review.response.ReviewResponse;
import com.example.dishpatch.domain.review.exception.ReviewErrorCode;
import com.example.dishpatch.global.exception.BizException;
import com.example.dishpatch.global.security.UserAuth;
import com.example.dishpatch.infra.db.menu.entity.Menu;
import com.example.dishpatch.infra.db.menu.repository.MenuRepository;
import com.example.dishpatch.infra.db.order.entity.Order;
import com.example.dishpatch.infra.db.order.repository.OrderRepository;
import com.example.dishpatch.infra.db.review.entity.Review;
import com.example.dishpatch.infra.db.review.entity.ReviewStatus;
import com.example.dishpatch.infra.db.review.repository.ReviewRepository;
import com.example.dishpatch.infra.db.store.entity.Category;
import com.example.dishpatch.infra.db.store.entity.Store;
import com.example.dishpatch.infra.db.store.repository.StoreRepository;
import com.example.dishpatch.infra.db.user.entity.User;
import com.example.dishpatch.infra.db.user.entity.UserGrade;
import com.example.dishpatch.infra.db.user.entity.UserProvider;
import com.example.dishpatch.infra.db.user.entity.UserRole;
import com.example.dishpatch.infra.db.user.repository.UserRepository;

@Nested
@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

	@Mock
	private ReviewRepository reviewRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private OrderRepository orderRepository;
	@Mock
	private StoreRepository storeRepository;
	@Mock
	private MenuRepository menuRepository;
	@InjectMocks
	private ReviewService reviewService;

	private User user;
	private Store store;
	private Menu menu;
	private Order order;
	private UserAuth userAuth;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		UserAuth userAuth = new UserAuth(1L, UserRole.USER);

		User user = new User("test@test.com", "password123", "01011112222", "userName",
			UserProvider.LOCAL, UserGrade.A, UserRole.USER, "userAddress");
		ReflectionTestUtils.setField(user, "id", 1L);

		User ceoUser = new User("ceo@test.com", "password456", "01033334444", "ceoName",
			UserProvider.LOCAL, UserGrade.A, UserRole.CEO, "ceoAddress");
		ReflectionTestUtils.setField(ceoUser, "id", 2L);

		Category category = new Category();
		ReflectionTestUtils.setField(category, "id", 1L);
		ReflectionTestUtils.setField(category, "name", "한식");

		Store store = new Store("storeName", "storeAddress", "storeImage", "0212345678", 2000,
			"storeIntro", 1500, true, LocalTime.NOON, LocalTime.MIDNIGHT, category, ceoUser);
		ReflectionTestUtils.setField(store, "id", 1L);

		Menu menu = Menu.builder()
			.name("menuName")
			.price(12000)
			.imageUrl("menuImage")
			.soldOut(false)
			.store(store)
			.options(List.of())
			.build();
		ReflectionTestUtils.setField(menu, "id", 1L);

		Order order = new Order();
		ReflectionTestUtils.setField(order, "id", 1L);
		ReflectionTestUtils.setField(order, "store", store);
		ReflectionTestUtils.setField(order, "status", "FINISHED");
	}

	@Test
	@DisplayName("배달 완료된 주문만 리뷰 작성 예외처리")
	void review_not_allowed_before_delivery() {
		// given
		ReviewCreateRequest request = new ReviewCreateRequest(1L, 1L, 5, "맛있어요", "imageUrl", ReviewStatus.PUBLIC);

		Order delivering = new Order();
		ReflectionTestUtils.setField(delivering, "id", 1L);
		ReflectionTestUtils.setField(delivering, "store", store);
		ReflectionTestUtils.setField(delivering, "status", "DELIVERING");

		given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
		given(orderRepository.findById(anyLong())).willReturn(Optional.of(delivering));
		given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
		given(menuRepository.findById(anyLong())).willReturn(Optional.of(menu));

		// when & then
		BizException exception = assertThrows(BizException.class, () -> reviewService.createReview(request, userAuth));

		assertEquals(ReviewErrorCode.REVIEW_NOT_ALLOWED_BEFORE_DELIVERY, exception.getErrorCode());
	}

	@Test
	@DisplayName("리뷰 작성 성공")
	void create_review_success() {
		// given
		ReviewCreateRequest request = new ReviewCreateRequest(1L, 1L, 5, "맛있어요", "imageUrl", ReviewStatus.PUBLIC);

		given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
		given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));
		given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
		given(menuRepository.findById(anyLong())).willReturn(Optional.of(menu));
		given(reviewRepository.save(any(Review.class))).willAnswer(invocation -> {
			Review saved = invocation.getArgument(0);
			ReflectionTestUtils.setField(saved, "id", 1L);
			return saved;
		});

		// when
		ReviewResponse response = reviewService.createReview(request, userAuth);

		// then
		assertThat(response).isNotNull();
		assertThat(response.rating()).isEqualTo(5);
	}

	@Test
	@DisplayName("리뷰 조회 성공")
	void find_reviews_success() {
		// given
		Review review = new Review(user, store, menu, order, 5, "맛있어요", "imageUrl", ReviewStatus.PUBLIC);
		ReflectionTestUtils.setField(review, "id", 1L);

		given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
		given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
		given(reviewRepository.findAllByStoreIdAndRating(anyLong(), anyLong(), anyInt(), anyInt(), any()))
			.willReturn(new PageImpl<>(List.of(review), PageRequest.of(0, 10), 1));

		// when
		ReviewPageResponse response = reviewService.findReviews(1L, null, null, userAuth, 0, 10);

		// then
		assertThat(response).isNotNull();
		assertThat(response.content()).hasSize(1);
	}

	@Test
	@DisplayName("리뷰 수정 성공")
	void update_review_success() {
		// given
		ReviewUpdateRequest request = new ReviewUpdateRequest(4, "좋아요", "newImageUrl", ReviewStatus.PUBLIC);

		Review review = new Review(user, store, menu, order, 5, "맛있어요", "imageUrl", ReviewStatus.PUBLIC);
		ReflectionTestUtils.setField(review, "id", 1L);

		given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
		given(reviewRepository.findById(anyLong())).willReturn(Optional.of(review));

		// when
		ReviewResponse response = reviewService.updateReview(1L, request, userAuth);

		// then
		assertThat(response).isNotNull();
		assertThat(response.rating()).isEqualTo(4);
		assertThat(response.contents()).isEqualTo("좋아요");
	}

	@Test
	@DisplayName("다른 사용자가 리뷰 삭제 시 예외 발생")
	void delete_review_author_mismatch() {
		// given
		User otherUser = new User("other@test.com", "password789", "01055556666", "otherName",
			UserProvider.LOCAL, UserGrade.B, UserRole.USER, "otherAddress");
		ReflectionTestUtils.setField(otherUser, "id", 999L);

		Review review = new Review(otherUser, store, menu, order, 5, "맛있어요", "imageUrl", ReviewStatus.PUBLIC);
		ReflectionTestUtils.setField(review, "id", 1L);

		given(reviewRepository.findById(anyLong())).willReturn(Optional.of(review));

		// when & then
		assertThrows(RuntimeException.class, () -> reviewService.deleteReview(1L, userAuth));
	}

	@Test
	@DisplayName("리뷰 삭제 성공")
	void delete_review_success() {
		// given
		Review review = new Review(user, store, menu, order, 5, "맛있어요", "imageUrl", ReviewStatus.PUBLIC);
		ReflectionTestUtils.setField(review, "id", 1L);

		given(reviewRepository.findById(anyLong())).willReturn(Optional.of(review));

		// when
		reviewService.deleteReview(1L, userAuth);

		// then
		then(reviewRepository).should(times(1)).deleteById(1L);
	}
}