package com.example.dishpatch.api.user.response;

import com.example.dishpatch.infra.db.user.entity.User;
import lombok.Getter;

@Getter
public class UserSignupResponse {

    private final String email;
    private final String name;
    private final String phone;
    private final String currentAddress;

    public UserSignupResponse(User user) {
        this.email = getEmail();
        this.name = getName();
        this.phone = getPhone();
        this.currentAddress = getCurrentAddress();
    }
}
