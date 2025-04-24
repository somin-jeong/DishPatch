package com.example.dishpatch.global.security;

import com.example.dishpatch.infra.db.user.entity.UserRole;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserAuth {
	private final Long id;
	private final UserRole role;
}
