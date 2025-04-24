package com.example.dishpatch.api.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dishpatch.api.user.request.UserDeleteRequest;
import com.example.dishpatch.api.user.request.UserLoginRequest;
import com.example.dishpatch.api.user.request.UserSignupRequest;
import com.example.dishpatch.api.user.request.UserUpdateRequest;
import com.example.dishpatch.api.user.response.UserLoginResponse;
import com.example.dishpatch.api.user.response.UserSignupResponse;
import com.example.dishpatch.api.user.response.UserUpdateResponse;
import com.example.dishpatch.domain.user.service.UserService;
import com.example.dishpatch.global.security.UserAuth;

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
	public ResponseEntity<Void> logout(HttpServletRequest request) {

		userService.logout(request);

		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PatchMapping
	public ResponseEntity<UserUpdateResponse> updateUser(
		@Valid @RequestBody UserUpdateRequest dto,
		@AuthenticationPrincipal UserAuth userAuth) {

		UserUpdateResponse userUpdateResponse = userService.updateUser(dto, userAuth);

		return ResponseEntity.status(HttpStatus.OK).body(userUpdateResponse);
	}

	@DeleteMapping
	public ResponseEntity<Void> deleteUser(@RequestBody UserDeleteRequest request, @AuthenticationPrincipal UserAuth userAuth){

		userService.deleteUser(request,userAuth);

		return ResponseEntity.status(HttpStatus.OK).build();

	}
}
