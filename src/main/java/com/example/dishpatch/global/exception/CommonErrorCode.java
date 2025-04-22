package com.example.dishpatch.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode {
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST.value(), "C001", "입력값이 올바르지 않습니다.");

    private final int status;
    private final String code;
    private final String message;
}
