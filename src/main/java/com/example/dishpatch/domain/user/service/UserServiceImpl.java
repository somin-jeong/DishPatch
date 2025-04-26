package com.example.dishpatch.domain.user.service;

import java.awt.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dishpatch.api.user.request.UserDeleteRequest;
import com.example.dishpatch.api.user.request.UserLoginRequest;
import com.example.dishpatch.api.user.request.UserSignupRequest;
import com.example.dishpatch.api.user.request.UserUpdateRequest;
import com.example.dishpatch.api.user.response.UserLoginResponse;
import com.example.dishpatch.api.user.response.UserSignupResponse;
import com.example.dishpatch.api.user.response.UserUpdateResponse;
import com.example.dishpatch.domain.store.service.StoreService;
import com.example.dishpatch.domain.user.exception.UserErrorCode;
import com.example.dishpatch.global.security.JwtUtil;
import com.example.dishpatch.global.exception.BizException;
import com.example.dishpatch.global.security.UserAuth;
import com.example.dishpatch.infra.db.cart.repository.CartRepository;
import com.example.dishpatch.infra.db.coupon.entity.Coupon;
import com.example.dishpatch.infra.db.coupon.repository.CouponRepository;
import com.example.dishpatch.infra.db.menu.repository.MenuOptionRepository;
import com.example.dishpatch.infra.db.menu.repository.MenuRepository;
import com.example.dishpatch.infra.db.order.entity.Order;
import com.example.dishpatch.infra.db.order.repository.OrderRepository;
import com.example.dishpatch.infra.db.pointHistory.entity.PointHistory;
import com.example.dishpatch.infra.db.pointHistory.repository.PointHistoryRepository;
import com.example.dishpatch.infra.db.review.entity.CeoReview;
import com.example.dishpatch.infra.db.review.repository.CeoReviewRepository;
import com.example.dishpatch.infra.db.review.repository.ReviewRepository;
import com.example.dishpatch.infra.db.store.repository.DibRepository;
import com.example.dishpatch.infra.db.store.repository.StoreRepository;
import com.example.dishpatch.infra.db.user.entity.User;
import com.example.dishpatch.infra.db.user.entity.UserGrade;
import com.example.dishpatch.infra.db.user.entity.UserProvider;
import com.example.dishpatch.infra.db.user.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;
	private final RedisTemplate<String, String> redisTemplate;
	private final OrderRepository orderRepository;
	private final PointHistoryRepository pointHistoryRepository;
	private final CouponRepository couponRepository;
	private final CartRepository cartRepository;
	private final StoreRepository storeRepository;
	private final DibRepository dibRepository;
	private final ReviewRepository reviewRepository;
	private final CeoReviewRepository ceoReviewRepository;
	private final MenuRepository menuRepository;
	private final MenuOptionRepository menuOptionRepository;


	@Override
	public UserSignupResponse signUp(UserSignupRequest request) {

		if (userRepository.findByEmail(request.email()).isPresent()) {
			throw new BizException(UserErrorCode.INVALID_EMAIL);
		}

		String encodedPassword = passwordEncoder.encode(request.password());

		User user = new User(request.email(), encodedPassword, request.phone(), request.name(), UserProvider.LOCAL,
			UserGrade.D, request.role(), request.currentAddress());

		User save = userRepository.save(user);

		return UserSignupResponse.from(save);
	}

	@Override
	public UserLoginResponse login(UserLoginRequest request) {

		User user = userRepository.findByEmail(request.email()).orElseThrow(
			() -> new BizException(UserErrorCode.INVALID_EMAIL));

		if (user.getDeletedDate()!=null) {
			throw new BizException(UserErrorCode.INVALID_ID);
		}

		if (!passwordEncoder.matches(request.password(), user.getPassword())) {
			throw new BizException(UserErrorCode.INVALID_PASSWORD);
		}


		String token = jwtUtil.createToken(user.getId(),user.getRole());

		return new UserLoginResponse(token);
	}

	@Override
	public void logout(HttpServletRequest request) {

		String token = jwtUtil.extractToken(request);
		// 토큰이 없을시 오류 반환
		if (token == null) {
			throw new BizException(UserErrorCode.INVALID_REQUEST);
		}

		long expiration = jwtUtil.getExpiration(token);
		/**
		 * 만료기간이 남았을때 blacklist에 등록
		 * "blacklist:"와 같은 prefix를 붙이는 이유는 Redis 내 다른 데이터와 충돌 방지
		 * Redis 내 검색을 용이하게 만듬
		 */
		if (expiration > 0) {
			redisTemplate.opsForValue().set("blacklist:" + token, "logout", expiration, TimeUnit.MILLISECONDS);
		}
	}

	@Transactional
	@Override
	public UserUpdateResponse updateUser(UserUpdateRequest dto, UserAuth userAuth) {

		User user = userRepository.findById(userAuth.getId()).orElseThrow(
			() -> new BizException(UserErrorCode.INVALID_ID));

		String encodedpassword = passwordEncoder.encode(dto.password());

		user.updateUser(encodedpassword, dto.name(), dto.phone(), dto.currentAddress());

		return UserUpdateResponse.from(user);
	}

	@Transactional
	@Override
	public void deleteUser(UserDeleteRequest request,UserAuth userAuth) {

		User user = userRepository.findById(userAuth.getId()).orElseThrow(
			() -> new BizException(UserErrorCode.INVALID_ID));

		if(!passwordEncoder.matches(request.password(),user.getPassword())){
			throw new BizException(UserErrorCode.INVALID_PASSWORD);
		}

		// coupons,menu,menu_options,order,point_history,store
		user.softDelete();

		ceoReviewRepository.deleteAllByUserId(userAuth.getId());// 리뷰 대댓
		reviewRepository.deleteAllByUserId(userAuth.getId()); // 리뷰
		cartRepository.deleteAllByUserId(userAuth.getId()); // 장바구니
		menuOptionRepository.deleteByUserId(userAuth.getId()); // 메뉴 옵션
		menuRepository.deleteByUserId(userAuth.getId());  // 메뉴
		orderRepository.deleteByUserId(userAuth.getId());  // 주문
		dibRepository.deleteByUserId(userAuth.getId());  // 찜
		storeRepository.deleteByUserId(userAuth.getId());  //가게
		pointHistoryRepository.deleteByUserId(userAuth.getId()); //포인트
		couponRepository.deleteByUserId(userAuth.getId());  //쿠폰

	}

	@Override
	public void updateUserProfileImage(UserAuth userAuth, String imageUrl) {

		User user = userRepository.findById(userAuth.getId()).orElseThrow(
			() -> new BizException(UserErrorCode.INVALID_ID));

		user.setImageUrl(imageUrl);
		userRepository.save(user);
	}
}
