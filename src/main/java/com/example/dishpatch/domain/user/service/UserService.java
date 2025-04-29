package com.example.dishpatch.domain.user.service;

import com.example.dishpatch.api.user.request.UserDeleteRequest;
import com.example.dishpatch.api.user.request.UserLoginRequest;
import com.example.dishpatch.api.user.request.UserSignupRequest;
import com.example.dishpatch.api.user.request.UserUpdateRequest;
import com.example.dishpatch.api.user.response.UserLoginResponse;
import com.example.dishpatch.api.user.response.UserProfileResponse;
import com.example.dishpatch.api.user.response.UserSignupResponse;
import com.example.dishpatch.api.user.response.UserUpdateResponse;
import com.example.dishpatch.global.security.UserAuth;

import jakarta.servlet.http.HttpServletRequest;

public interface UserService {
	UserSignupResponse signUp(UserSignupRequest request);

	UserLoginResponse login(UserLoginRequest request);

	void logout(String token);

	UserUpdateResponse updateUser(UserUpdateRequest dto, UserAuth userAuth);

	void deleteUser(UserDeleteRequest request,UserAuth userAuth);

	void updateUserProfileImage(UserAuth userAuth, String imageUrl);

	UserProfileResponse getUserProfile(UserAuth userAuth);
}
