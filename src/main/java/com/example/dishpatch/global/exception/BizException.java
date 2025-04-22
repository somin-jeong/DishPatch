package com.example.dishpatch.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BizException extends RuntimeException {

    private ErrorCode errorCode;
}
