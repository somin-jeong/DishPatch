package com.example.dishpatch.domain.coupon.exception;

import org.springframework.http.HttpStatus;

import com.example.dishpatch.global.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponErrorCode implements ErrorCode {

	COUPON_EXCEEDS_TOTAL(HttpStatus.BAD_REQUEST.value(), "C001", "쿠폰이 총 금액보다 큽니다."),
	COUPON_NOT_EXIST(HttpStatus.BAD_REQUEST.value(), "C002", "쿠폰이 존재하지 않습니다."),
	COUPON_EXPIRED(HttpStatus.BAD_REQUEST.value(), "C003", "이미 만료된 쿠폰입니다.");

	private final int status;
	private final String code;
	private final String message;
}
