package com.example.dishpatch.domain.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.dishpatch.api.user.request.UserDeleteRequest;
import com.example.dishpatch.api.user.request.UserLoginRequest;
import com.example.dishpatch.api.user.request.UserSignupRequest;
import com.example.dishpatch.api.user.request.UserUpdateRequest;
import com.example.dishpatch.api.user.response.UserLoginResponse;
import com.example.dishpatch.api.user.response.UserSignupResponse;
import com.example.dishpatch.api.user.response.UserUpdateResponse;
import com.example.dishpatch.global.security.JwtUtil;
import com.example.dishpatch.global.security.UserAuth;
import com.example.dishpatch.infra.db.cart.repository.CartRepository;
import com.example.dishpatch.infra.db.coupon.repository.CouponRepository;
import com.example.dishpatch.infra.db.menu.repository.MenuOptionRepository;
import com.example.dishpatch.infra.db.menu.repository.MenuRepository;
import com.example.dishpatch.infra.db.order.repository.OrderRepository;
import com.example.dishpatch.infra.db.pointHistory.repository.PointHistoryRepository;
import com.example.dishpatch.infra.db.review.repository.CeoReviewRepository;
import com.example.dishpatch.infra.db.review.repository.ReviewRepository;
import com.example.dishpatch.infra.db.store.repository.DibRepository;
import com.example.dishpatch.infra.db.store.repository.StoreRepository;
import com.example.dishpatch.infra.db.user.entity.User;
import com.example.dishpatch.infra.db.user.entity.UserGrade;
import com.example.dishpatch.infra.db.user.entity.UserProvider;
import com.example.dishpatch.infra.db.user.entity.UserRole;
import com.example.dishpatch.infra.db.user.repository.RedisRepository;
import com.example.dishpatch.infra.db.user.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
	@Mock
	private UserRepository userRepository;
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private JwtUtil jwtUtil;
	@Mock
	private ValueOperations<String, String> valueOperations;
	@Mock
	private RedisTemplate<String,String> redisTemplate;
	@Mock
	private CeoReviewRepository ceoReviewRepository;
	@Mock
	private ReviewRepository reviewRepository;
	@Mock
	private CartRepository cartRepository;
	@Mock
	private MenuOptionRepository menuOptionRepository;
	@Mock
	private MenuRepository menuRepository;
	@Mock
	private OrderRepository orderRepository;
	@Mock
	private DibRepository dibRepository;
	@Mock
	private StoreRepository storeRepository;
	@Mock
	private PointHistoryRepository pointHistoryRepository;
	@Mock
	private CouponRepository couponRepository;


	@InjectMocks
	private UserServiceImpl userServiceImpl;

	@Test
	void signUp() {
		// given
		UserSignupRequest request = new UserSignupRequest(
			"test@naver.com",
			"Aa123456!",
			"테스트이름",
			"01012345678",
			"테스트주소",
			UserRole.USER
		);

		given(userRepository.findByEmail(request.email()))
			.willReturn(Optional.empty());

		given(passwordEncoder.encode(request.password()))
			.willReturn("encodedPassword123!");

		User user = new User(
			request.email(),
			"encodedPassword123!",
			request.phone(),
			request.name(),
			UserProvider.LOCAL,
			UserGrade.D,
			request.role(),
			request.currentAddress()
		);

		given(userRepository.save(any(User.class)))
			.willReturn(user);


		// when
		UserSignupResponse response = userServiceImpl.signUp(request);

		// then
		assertEquals(request.email(),response.email());
		assertEquals(request.name(),response.name());
		assertEquals(request.phone(),response.phone());

	}

	@Test
	void login() {
		// given
		User user = new User(
			"test@naver.com",
			"encodedPassword123!",
			"01012345678",
			"테스트이름",
			UserProvider.LOCAL,
			UserGrade.D,
			UserRole.USER,
			"테스트주소"
		);
		// id가 자동생성이기 때문에 강제세팅
		ReflectionTestUtils.setField(user,"id",1L);

		UserLoginRequest request = new UserLoginRequest(
			"test@naver.com",
			"Aa123456!"
		);

		given(userRepository.findByEmail(request.email()))
			.willReturn(Optional.of(user));
		given(passwordEncoder.matches(request.password(),user.getPassword()))
			.willReturn(true);
		/**
		 * any()같은 Matcher를 써야하는 경우 모든 파라미터를 다 Matcher로 통일해야함 그렇기에 UserRole.User를 그대로
		 * 사용하기 보단 eq()로 감싸서 사용할 것
		 * eq()는 Matcher의 한 종류로 '이 인자는 정확히 이값이어야 한다'는 의미로 쓰임
		 */

		given(jwtUtil.createToken(any(Long.class),eq(UserRole.USER)))
			.willReturn("testToken");

		// when
		UserLoginResponse response = userServiceImpl.login(request);

		// then
		assertEquals("testToken",response.token());
	}

	@Test
	void logout() {
		// given
		String token = "testToken";

		// 10분 남았다고 가정
		given(jwtUtil.getExpiration(token))
			.willReturn(600000L);

		// when
		given(redisTemplate.opsForValue())
			.willReturn(valueOperations);

		userServiceImpl.logout(token);

		// then
		verify(valueOperations,times(1)).set(
			"blacklist:" + token, "logout", 600000L, TimeUnit.MILLISECONDS);
	}

	@Test
	void updateUser() {
		// given
		User user = new User(
			"test@naver.com",
			"encodedPassword123!",
			"01012345678",
			"테스트이름",
			UserProvider.LOCAL,
			UserGrade.D,
			UserRole.USER,
			"테스트주소"
		);
		ReflectionTestUtils.setField(user,"id",1L);

		UserAuth userAuth = new UserAuth(1L,UserRole.USER);

		UserUpdateRequest request = new UserUpdateRequest(
			"Modifiedpass123!",
			"수정된 이름",
			"01087654321",
			"수정된 주소"
		);

		given(userRepository.findById(userAuth.getId()))
			.willReturn(Optional.of(user));

		// when
		UserUpdateResponse response = userServiceImpl.updateUser(request, userAuth);

		// then
		assertEquals(request.name(),response.name());
		assertEquals(request.currentAddress(),response.currentAddress());
		assertEquals(request.phone(),response.phone());
	}

	@Test
	void deleteUser() {
		// given
		User user = mock(User.class);
		UserDeleteRequest request = new UserDeleteRequest("password");
		UserAuth userAuth = new UserAuth(1L,UserRole.USER);

		given(userRepository.findById(userAuth.getId()))
			.willReturn(Optional.of(user));

		given(passwordEncoder.matches(request.password(),user.getPassword()))
			.willReturn(true);

		// when
		userServiceImpl.deleteUser(request,userAuth);

		// then
		verify(user,times(1)).softDelete();
		verify(ceoReviewRepository,times(1)).deleteAllByUserId(userAuth.getId());
		verify(reviewRepository,times(1)).deleteAllByUserId(userAuth.getId());
		verify(cartRepository,times(1)).deleteAllByUserId(userAuth.getId());
		verify(menuOptionRepository,times(1)).deleteByUserId(userAuth.getId());
		verify(menuRepository,times(1)).deleteByUserId(userAuth.getId());
		verify(orderRepository,times(1)).deleteByUserId(userAuth.getId());
		verify(dibRepository,times(1)).deleteByUserId(userAuth.getId());
		verify(storeRepository,times(1)).deleteByUserId(userAuth.getId());
		verify(pointHistoryRepository,times(1)).deleteByUserId(userAuth.getId());
		verify(couponRepository,times(1)).deleteByUserId(userAuth.getId());

	}
}