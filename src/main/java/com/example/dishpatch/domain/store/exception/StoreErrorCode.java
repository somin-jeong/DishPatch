package com.example.dishpatch.domain.store.exception;

import org.springframework.http.HttpStatus;

import com.example.dishpatch.global.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StoreErrorCode implements ErrorCode {
	STORE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "S001", "존재하지 않는 가게입니다."),
	CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "S002", "존재하지 않는 카테고리입니다."),
	STORE_OWN_LIMIT_EXCEEDED(HttpStatus.NOT_FOUND.value(), "S003", "사장은 가게를 3개 이상 소유할 수 없습니다."),
	ALREADY_DIB_STORE(HttpStatus.CONFLICT.value(), "S004", "이미 찜한 가게입니다."),
	UNDIB_STORE(HttpStatus.CONFLICT.value(), "S005", "찜하지 않은 가게입니다."),
	STORE_OWNER_MISMATCH(HttpStatus.FORBIDDEN.value(), "S006", "가게에 대한 권한이 없습니다.");

	private final int status;
	private final String code;
	private final String message;
}
