package com.example.dishpatch.domain.cart.exception;

import org.springframework.http.HttpStatus;

import com.example.dishpatch.global.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CartErrorCode implements ErrorCode {
	CART_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "C001", "존재하지 않는 장바구니입니다."),
	INVALID_QUANTITY(HttpStatus.BAD_REQUEST.value(), "C002", "수량은 1개 이상입니다."),
	CART_AUTHOR_MISMATCH(HttpStatus.FORBIDDEN.value(), "C003", "장바구니 사용자와 일치하지 않습니다.");

	private final int status;
	private final String code;
	private final String message;
}

