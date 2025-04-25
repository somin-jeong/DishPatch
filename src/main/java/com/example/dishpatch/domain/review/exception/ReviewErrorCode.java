package com.example.dishpatch.domain.review.exception;

import org.springframework.http.HttpStatus;

import com.example.dishpatch.global.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReviewErrorCode implements ErrorCode {

	REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "R001", "존재하지 않는 리뷰입니다."),
	REVIEW_AUTHOR_MISMATCH(HttpStatus.FORBIDDEN.value(), "R002", "리뷰 작성자와 일치하지 않습니다."),
	REVIEW_NOT_ALLOWED_BEFORE_DELIVERY(HttpStatus.BAD_REQUEST.value(), "R003", "배달 완료되지 않은 주문에 리뷰를 작성할 수 없습니다.");

	private final int status;
	private final String code;
	private final String message;
}
