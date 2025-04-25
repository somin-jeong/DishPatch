package com.example.dishpatch.domain.menu.exception;

import org.springframework.http.HttpStatus;

import com.example.dishpatch.global.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MenuErrorCode implements ErrorCode {

	MENU_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "M001", "존재하지 않는 메뉴입니다."),
	MENU_NOT_BELONG_TO_STORE(HttpStatus.BAD_REQUEST.value(), "M002", "해당 메뉴는 요청한 가게에 속하지 않습니다.");

	private final int status;
	private final String code;
	private final String message;
}
