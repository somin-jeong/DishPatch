package com.example.dishpatch.domain.review.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

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
import org.springframework.test.util.ReflectionTestUtils;

import com.example.dishpatch.api.review.request.CeoReviewCreateRequest;
import com.example.dishpatch.api.review.request.CeoReviewUpdateRequest;
import com.example.dishpatch.api.review.response.CeoReviewResponse;
import com.example.dishpatch.domain.review.exception.CeoReviewErrorCode;
import com.example.dishpatch.domain.review.exception.ReviewErrorCode;
import com.example.dishpatch.global.exception.BizException;
import com.example.dishpatch.global.security.UserAuth;
import com.example.dishpatch.infra.db.review.entity.CeoReview;
import com.example.dishpatch.infra.db.review.entity.Review;
import com.example.dishpatch.infra.db.review.entity.ReviewStatus;
import com.example.dishpatch.infra.db.review.repository.CeoReviewRepository;
import com.example.dishpatch.infra.db.review.repository.ReviewRepository;
import com.example.dishpatch.infra.db.user.entity.User;
import com.example.dishpatch.infra.db.user.entity.UserGrade;
import com.example.dishpatch.infra.db.user.entity.UserProvider;
import com.example.dishpatch.infra.db.user.entity.UserRole;
import com.example.dishpatch.infra.db.user.repository.UserRepository;

@Nested
@ExtendWith(MockitoExtension.class)
class CeoReviewServiceTest {

	@Mock
	private UserRepository userRepository;
	@Mock
	private ReviewRepository reviewRepository;
	@Mock
	private CeoReviewRepository ceoReviewRepository;
	@InjectMocks
	private CeoReviewService ceoReviewService;

	private User ceoUser;
	private Review review;
	private CeoReview ceoReview;
	private UserAuth userAuth;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		ceoUser = new User("ceo@test.com", "password123", "01011112222", "ceoName",
			UserProvider.LOCAL, UserGrade.A, UserRole.CEO, "ceoAddress");
		ReflectionTestUtils.setField(ceoUser, "id", 1L);

		review = new Review();
		ReflectionTestUtils.setField(review, "id", 1L);

		ceoReview = new CeoReview(ceoUser, "사장님 댓글", ReviewStatus.PUBLIC, review);
		ReflectionTestUtils.setField(ceoReview, "id", 1L);

		userAuth = new UserAuth(1L, UserRole.CEO);
	}

	@Test
	@DisplayName("존재하지 않는 리뷰에 댓글 작성 시 예외")
	void createCeoReviewReviewNotFound() {
		// given
		CeoReviewCreateRequest request = new CeoReviewCreateRequest("감사합니다", ReviewStatus.PUBLIC);

		given(reviewRepository.findById(anyLong())).willReturn(Optional.empty());

		// when & then
		BizException exception = assertThrows(BizException.class, () -> ceoReviewService.createCeoReview(1L, request));
		assertEquals(ReviewErrorCode.REVIEW_NOT_FOUND, exception.getErrorCode());
	}

	@Test
	@DisplayName("사장님 댓글 작성 성공")
	void create_ceo_ceview_success() {
		// given
		CeoReviewCreateRequest request = new CeoReviewCreateRequest("감사합니다!", ReviewStatus.PUBLIC);

		given(reviewRepository.findById(anyLong())).willReturn(Optional.of(review));
		given(userRepository.findById(anyLong())).willReturn(Optional.of(ceoUser));
		given(ceoReviewRepository.save(any(CeoReview.class))).willAnswer(invocation -> {
			CeoReview saved = invocation.getArgument(0);
			ReflectionTestUtils.setField(saved, "id", 1L);
			return saved;
		});

		// when
		CeoReviewResponse response = ceoReviewService.createCeoReview(1L, request);

		// then
		assertThat(response).isNotNull();
		assertThat(response.contents()).isEqualTo("감사합니다!");
	}

	@Test
	@DisplayName("존재하지 않는 사장님 댓글 수정 시 예외")
	void update_ceo_review_not_found() {
		// given
		CeoReviewUpdateRequest request = new CeoReviewUpdateRequest("수정된 내용", ReviewStatus.PRIVATE);

		given(reviewRepository.findById(anyLong())).willReturn(Optional.of(review));
		given(ceoReviewRepository.findById(anyLong())).willReturn(Optional.empty());

		// when & then
		BizException exception = assertThrows(BizException.class,
			() -> ceoReviewService.updateCeoReview(1L, 1L, request));
		assertEquals(CeoReviewErrorCode.CEO_REVIEW_NOT_FOUND, exception.getErrorCode());
	}

	@Test
	@DisplayName("사장님 댓글 수정 성공")
	void update_ceo_review_success() {
		// given
		CeoReviewUpdateRequest request = new CeoReviewUpdateRequest("수정된 내용", ReviewStatus.PRIVATE);

		given(reviewRepository.findById(anyLong())).willReturn(Optional.of(review));
		given(ceoReviewRepository.findById(anyLong())).willReturn(Optional.of(ceoReview));

		// when
		CeoReviewResponse response = ceoReviewService.updateCeoReview(1L, 1L, request);

		// then
		assertThat(response).isNotNull();
		assertThat(response.contents()).isEqualTo("수정된 내용");
		assertThat(response.status()).isEqualTo(ReviewStatus.PRIVATE);
	}

	@Test
	@DisplayName("다른 사용자가 댓글 삭제 시 예외 발생")
	void deleteCeoReviewAuthorMismatch() {
		// given
		User anotherUser = new User("other@test.com", "password456", "01055556666", "otherName",
			UserProvider.LOCAL, UserGrade.B, UserRole.CEO, "otherAddress");
		ReflectionTestUtils.setField(anotherUser, "id", 2L);

		CeoReview otherCeoReview = new CeoReview(anotherUser, "다른 사람 댓글", ReviewStatus.PUBLIC, review);
		ReflectionTestUtils.setField(otherCeoReview, "id", 2L);

		given(reviewRepository.findById(anyLong())).willReturn(Optional.of(review));
		given(ceoReviewRepository.findById(anyLong())).willReturn(Optional.of(otherCeoReview));

		// when & then
		BizException exception = assertThrows(BizException.class,
			() -> ceoReviewService.deleteCeoReview(1L, 2L, userAuth));
		assertEquals(CeoReviewErrorCode.CEO_REVIEW_AUTHOR_MISMATCH, exception.getErrorCode());
	}

	@Test
	@DisplayName("사장님 댓글 삭제 성공")
	void delete_ceo_review_success() {
		// given
		given(reviewRepository.findById(anyLong())).willReturn(Optional.of(review));
		given(ceoReviewRepository.findById(anyLong())).willReturn(Optional.of(ceoReview));

		// when
		ceoReviewService.deleteCeoReview(1L, 1L, userAuth);

		// then
		then(ceoReviewRepository).should(times(1)).deleteById(1L);
	}
}