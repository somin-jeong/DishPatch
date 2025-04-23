package com.example.dishpatch.domain.menu.exception;

import org.springframework.http.HttpStatus;

import com.example.dishpatch.global.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MenuErrorCode implements ErrorCode {

	INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST.value(), "C001", "입력값이 올바르지 않습니다.");

	private final int status;
	private final String code;
	private final String message;
}
