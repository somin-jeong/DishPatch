package com.example.dishpatch.domain.pointHistory.exception;

import org.springframework.http.HttpStatus;

import com.example.dishpatch.global.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PointErrorCode implements ErrorCode {

	INSUFFICIENT_POINT(HttpStatus.BAD_REQUEST.value(), "P001", "포인트가 부족합니다."),
	NO_POINT(HttpStatus.BAD_REQUEST.value(), "P002", "포인트가 없습니다."),
	EXCEEDING_POINT_AMOUNT(HttpStatus.BAD_REQUEST.value(), "P005", "사용하려는 포인트가 총 결제 금액보다 큽니다.");

	private final int status;
	private final String code;
	private final String message;
}
