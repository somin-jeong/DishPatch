package com.example.dishpatch.domain.user.service;


import com.example.dishpatch.api.user.response.UserSignupResponse;
import com.example.dishpatch.infra.db.user.entity.UserRole;

public interface UserService {
    UserSignupResponse signUp(String email, String password, String name, String phone, String currentAddress, UserRole role);
}
