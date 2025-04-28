package com.example.dishpatch.api.user.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.dishpatch.api.user.request.UserDeleteRequest;
import com.example.dishpatch.api.user.request.UserLoginRequest;
import com.example.dishpatch.api.user.request.UserSignupRequest;
import com.example.dishpatch.api.user.request.UserUpdateRequest;
import com.example.dishpatch.api.user.response.UserLoginResponse;
import com.example.dishpatch.api.user.response.UserProfileResponse;
import com.example.dishpatch.api.user.response.UserSignupResponse;
import com.example.dishpatch.api.user.response.UserUpdateResponse;
import com.example.dishpatch.domain.user.service.UserService;
import com.example.dishpatch.global.S3.service.S3Service;
import com.example.dishpatch.global.security.UserAuth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private final S3Service s3Service;

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

	@PostMapping("/image")
	public ResponseEntity<String> uploadUserProfileFile(
		@AuthenticationPrincipal UserAuth userAuth,
		@RequestParam("file") MultipartFile file){

		try {
			String imageUrl = s3Service.uploadImage(file);

			userService.updateUserProfileImage(userAuth,imageUrl);

			return ResponseEntity.ok("파일이 정상적으로 업로드 되었습니다. url: " + imageUrl);
		} catch (IOException e) {
			// 어디서 어떤 에러가 터졌는지 콘솔에 찍어주는 함수
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 실패");
		}
	}

	@GetMapping
	public ResponseEntity<UserProfileResponse> getUserProfileFile(@AuthenticationPrincipal UserAuth userAuth){

		UserProfileResponse userProfile = userService.getUserProfile(userAuth);

		return ResponseEntity.status(HttpStatus.OK).body(userProfile);

	}

}
