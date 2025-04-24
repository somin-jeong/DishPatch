package com.example.dishpatch.domain.order.exception;

import org.springframework.http.HttpStatus;

import com.example.dishpatch.global.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderErrorCode implements ErrorCode {

	UNDER_MINIMUM_ORDER_PRICE(HttpStatus.BAD_REQUEST.value(), "O001", "주문하려는 금액이 최소주문금액보다 작습니다."),
	STORE_CLOSED(HttpStatus.BAD_REQUEST.value(), "O002", "매장이 영업 중이 아닙니다."),
	INVALID_ORDER_STATUS(HttpStatus.BAD_REQUEST.value(), "O003", "현재 주문 상태와 일치하지 않습니다."),
	ORDER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "O004", "존재하지 않는 주문입니다."),
	ORDER_ALREADY_FINISHED(HttpStatus.BAD_REQUEST.value(), "O005", "이미 완료된 주문입니다."),
	INVALID_ORDER_REJECTION(HttpStatus.BAD_REQUEST.value(), "O006", "거절할 수 없는 상태입니다."),
	INACCESSIBLE_ORDER(HttpStatus.FORBIDDEN.value(), "O007", "접근할 수 없는 주문입니다."),
	;

	private final int status;
	private final String code;
	private final String message;
}
