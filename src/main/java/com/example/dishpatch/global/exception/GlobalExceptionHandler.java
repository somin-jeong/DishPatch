package com.example.dishpatch.global.exception;

import com.example.dishpatch.global.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BizException.class)
    public ResponseEntity<ErrorResponse> handleBizError(BizException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ErrorResponse.of(e.getErrorCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationError(MethodArgumentNotValidException e) {
        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.of(CommonErrorCode.INVALID_INPUT_VALUE, e.getBindingResult()));
    }

}
