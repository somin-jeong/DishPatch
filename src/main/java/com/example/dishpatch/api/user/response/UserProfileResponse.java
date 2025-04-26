package com.example.dishpatch.api.user.response;

import com.example.dishpatch.infra.db.user.entity.User;

public record UserProfileResponse(
	String imageUrl
) {
	public static UserProfileResponse from(User user){
		return new UserProfileResponse(user.getImageUrl());
	}
}
