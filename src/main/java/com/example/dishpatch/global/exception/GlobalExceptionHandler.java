package com.example.dishpatch.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.dishpatch.global.response.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BizException.class)
	public ResponseEntity<ErrorResponse> handleBizError(BizException exception) {
		return ResponseEntity
			.status(exception.getErrorCode().getStatus())
			.body(ErrorResponse.of(exception.getErrorCode()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationError(MethodArgumentNotValidException exception) {
		return ResponseEntity
			.badRequest()
			.body(ErrorResponse.of(CommonErrorCode.INVALID_INPUT_VALUE, exception.getBindingResult()));
	}

}
