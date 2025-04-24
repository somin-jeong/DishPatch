package com.example.dishpatch.domain.menu.exception;

import org.springframework.http.HttpStatus;

import com.example.dishpatch.global.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MenuOptionErrorCode implements ErrorCode {

	MENU_OPTION_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "MO001", "존재하지 않는 메뉴 옵션입니다."),
	INVALID_MENU_OPTION_RELATION(HttpStatus.BAD_REQUEST.value(), "MO002", "해당 메뉴에 속하지 않는 옵션입니다.");

	private final int status;
	private final String code;
	private final String message;
}
