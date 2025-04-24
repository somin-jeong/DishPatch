package com.example.dishpatch.domain.user.exception;

import org.springframework.http.HttpStatus;

import com.example.dishpatch.global.exception.ErrorCode;

public enum UserErrorCode implements ErrorCode {

	INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "유효하지 않은 비밀번호입니다."),
	INVALID_EMAIL(HttpStatus.UNAUTHORIZED, "유효하지 않은 이메일입니다."),
	USER_ROLE_NOT_CEO(HttpStatus.FORBIDDEN, "사장 권한이 필요한 작업입니다.");

	private final HttpStatus httpStatus;
	private final String message;

	UserErrorCode(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}

	@Override
	public int getStatus() {
		return httpStatus.value();
	}

	@Override
	public String getCode() {
		return this.name();
	}

	@Override
	public String getMessage() {
		return message;
	}

}
