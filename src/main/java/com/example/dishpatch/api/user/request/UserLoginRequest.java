package com.example.dishpatch.api.user.request;

public record UserLoginRequest(
        String email,
        String password
) {
}
