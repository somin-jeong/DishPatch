package com.example.dishpatch.domain.statistics.exception;

import org.springframework.http.HttpStatus;

import com.example.dishpatch.global.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StatErrorCode implements ErrorCode {
	UNSUPPORTED_STAT_PERIOD(HttpStatus.NOT_FOUND.value(), "S001", "지원하지 않는 통계 주기입니다.");

	private final int status;
	private final String code;
	private final String message;
}
