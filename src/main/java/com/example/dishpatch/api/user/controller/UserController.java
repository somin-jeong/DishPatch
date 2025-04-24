package com.example.dishpatch.api.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dishpatch.api.user.request.UserLoginRequest;
import com.example.dishpatch.api.user.request.UserSignupRequest;
import com.example.dishpatch.api.user.response.UserLoginResponse;
import com.example.dishpatch.api.user.response.UserSignupResponse;
import com.example.dishpatch.domain.user.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping("/signup")
	public ResponseEntity<UserSignupResponse> signUp(@Valid @RequestBody UserSignupRequest request) {

		UserSignupResponse userSignupResponse = userService.signUp(request);

		return ResponseEntity.status(HttpStatus.CREATED).body(userSignupResponse);
	}

	@PostMapping("/login")
	public ResponseEntity<UserLoginResponse> login(@RequestBody UserLoginRequest request) {

		UserLoginResponse login = userService.login(request);

		return ResponseEntity.status(HttpStatus.OK).body(login);
	}

	@PostMapping("/logout")
	public ResponseEntity<String> logout(HttpServletRequest request) {

		userService.logout(request);

		return ResponseEntity.status(HttpStatus.OK).body("로그아웃 완료");
	}

}
