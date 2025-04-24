package com.example.dishpatch.api.order.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dishpatch.api.order.request.OrderRequestDto;
import com.example.dishpatch.api.order.response.OrderResponseDto;
import com.example.dishpatch.domain.order.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@PostMapping
	public ResponseEntity<OrderResponseDto> createOrder(
		HttpServletRequest request,
		@Valid @RequestBody OrderRequestDto requestDto
	) {
		Long userId = (Long)request.getSession().getAttribute("userId");

		OrderResponseDto responseDto = orderService.createOrder(requestDto, userId);

		return ResponseEntity.status(HttpStatus.OK).body(responseDto);
	}

	@PatchMapping("/{orderId}/update")
	public ResponseEntity<OrderResponseDto> updateOrder(
		HttpServletRequest request,
		@PathVariable Long orderId
	) {
		Long userId = (Long)request.getSession().getAttribute("userId");

		OrderResponseDto responseDto = orderService.updateOrder(userId, orderId);

		return ResponseEntity.status(HttpStatus.OK).body(responseDto);
	}

}
