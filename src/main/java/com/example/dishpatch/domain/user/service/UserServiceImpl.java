package com.example.dishpatch.domain.user.service;

import com.example.dishpatch.api.user.response.UserSignupResponse;
import com.example.dishpatch.global.config.SecurityConfig;
import com.example.dishpatch.infra.db.user.entity.User;
import com.example.dishpatch.infra.db.user.entity.UserGrade;
import com.example.dishpatch.infra.db.user.entity.UserProvider;
import com.example.dishpatch.infra.db.user.entity.UserRole;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.dishpatch.infra.db.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final SecurityConfig securityConfig;
	private final PasswordEncoder passwordEncoder;

	@Override
	public UserSignupResponse signUp(String email, String password, String name, String phone, String currentAddress,UserRole role) {

		if(userRepository.findByEmail(email).isPresent()){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 사용중인 이메일입니다.");
		}

		String encodedPassword = passwordEncoder.encode(password);

		User user = new User(email, encodedPassword, phone, name, UserProvider.LOCAL, UserGrade.D, role,currentAddress);

		User save = userRepository.save(user);

		return new UserSignupResponse(save);
	}
}
