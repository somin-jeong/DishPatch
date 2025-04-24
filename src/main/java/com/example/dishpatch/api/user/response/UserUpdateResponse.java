package com.example.dishpatch.api.user.response;

import com.example.dishpatch.infra.db.user.entity.User;

public record UserUpdateResponse(
	String name,
	String phone,
	String currentAddress
) {
	public static UserUpdateResponse from(User user){
		return new UserUpdateResponse(user.getName(), user.getPhone(), user.getCurrentAddress());
	}
}
