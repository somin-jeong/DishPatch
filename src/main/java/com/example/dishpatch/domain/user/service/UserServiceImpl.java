package com.example.dishpatch.domain.user.service;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dishpatch.api.user.request.UserLoginRequest;
import com.example.dishpatch.api.user.request.UserSignupRequest;
import com.example.dishpatch.api.user.request.UserUpdateRequest;
import com.example.dishpatch.api.user.response.UserLoginResponse;
import com.example.dishpatch.api.user.response.UserSignupResponse;
import com.example.dishpatch.api.user.response.UserUpdateResponse;
import com.example.dishpatch.domain.user.exception.UserErrorCode;
import com.example.dishpatch.global.config.JwtUtil;
import com.example.dishpatch.global.config.SecurityConfig;
import com.example.dishpatch.global.exception.BizException;
import com.example.dishpatch.infra.db.user.entity.User;
import com.example.dishpatch.infra.db.user.entity.UserGrade;
import com.example.dishpatch.infra.db.user.entity.UserProvider;
import com.example.dishpatch.infra.db.user.entity.UserStatus;
import com.example.dishpatch.infra.db.user.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final SecurityConfig securityConfig;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;
	private final RedisTemplate<String, String> redisTemplate;

	@Override
	public UserSignupResponse signUp(UserSignupRequest request) {

		if (userRepository.findByEmail(request.email()).isPresent()) {
			throw new BizException(UserErrorCode.INVALID_EMAIL);
		}

		String encodedPassword = passwordEncoder.encode(request.password());

		User user = new User(request.email(), encodedPassword, request.phone(), request.name(), UserProvider.LOCAL,
			UserGrade.D, request.role(), request.currentAddress(),
			UserStatus.ACTIVE);

		User save = userRepository.save(user);

		return UserSignupResponse.from(save);
	}

	@Override
	public UserLoginResponse login(UserLoginRequest request) {

		User user = userRepository.findByEmail(request.email()).orElseThrow(
			() -> new BizException(UserErrorCode.INVALID_EMAIL));

		if (!passwordEncoder.matches(request.password(), user.getPassword())) {
			throw new BizException(UserErrorCode.INVALID_PASSWORD);
		}

		String token = jwtUtil.createToken(user.getId());

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
	public UserUpdateResponse updateUser(UserUpdateRequest dto, HttpServletRequest request) {
		String token = jwtUtil.extractToken(request);
		Long id = jwtUtil.extractId(token);

		User user = userRepository.findById(id).orElseThrow(
			() -> new BizException(UserErrorCode.INVALID_ID));

		user.updateUser(dto.password(), dto.name(), dto.phone(), dto.currentAddress());

		return UserUpdateResponse.from(user);
	}
}
