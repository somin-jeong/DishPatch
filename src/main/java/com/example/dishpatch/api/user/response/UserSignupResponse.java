package com.example.dishpatch.api.user.response;

import com.example.dishpatch.infra.db.user.entity.User;
import lombok.Getter;

public record UserSignupResponse(
        String email,
        String name,
        String phone,
        String currentAddress
) {
    public static UserSignupResponse from(User user){
        return new UserSignupResponse(user.getEmail(),user.getName(),user.getPhone(),user.getCurrentAddress());
    }
}
