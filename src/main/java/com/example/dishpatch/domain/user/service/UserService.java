package com.example.dishpatch.domain.user.service;


import com.example.dishpatch.api.user.request.UserLoginRequest;
import com.example.dishpatch.api.user.request.UserSignupRequest;
import com.example.dishpatch.api.user.response.UserLoginResponse;
import com.example.dishpatch.api.user.response.UserSignupResponse;

public interface UserService {
    UserSignupResponse signUp(UserSignupRequest request);

    UserLoginResponse login(UserLoginRequest request);
}
