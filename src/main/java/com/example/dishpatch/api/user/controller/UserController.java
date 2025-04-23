package com.example.dishpatch.api.user.controller;

import com.example.dishpatch.api.user.request.UserSignupRequest;
import com.example.dishpatch.api.user.response.UserSignupResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dishpatch.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping("/signup")
	public ResponseEntity<UserSignupResponse> signUp(@Valid @RequestBody UserSignupRequest request){

		UserSignupResponse userSignupResponse = userService.signUp(
				request.email(), request.password(), request.name(), request.phone(), request.currentAddress(), request.role());

		return ResponseEntity.status(HttpStatus.CREATED).body(userSignupResponse);
	}

}
