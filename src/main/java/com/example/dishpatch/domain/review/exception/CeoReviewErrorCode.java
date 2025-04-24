package com.example.dishpatch.domain.review.exception;

import org.springframework.http.HttpStatus;

import com.example.dishpatch.global.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CeoReviewErrorCode implements ErrorCode {
	CEO_REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "CR001", "존재하지 않는 댓글입니다."),
	CEO_REVIEW_AUTHOR_MISMATCH(HttpStatus.FORBIDDEN.value(), "CR002", "댓글 작성자와 일치하지 않습니다.");

	private final int status;
	private final String code;
	private final String message;
}
