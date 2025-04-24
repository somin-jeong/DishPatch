package com.example.dishpatch.api.order.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dishpatch.api.order.request.OrderRequestDto;
import com.example.dishpatch.api.order.request.OrderStatusRequestDto;
import com.example.dishpatch.api.order.response.OrderDetailResponseDto;
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
		@PathVariable Long orderId,
		@Valid @RequestBody OrderStatusRequestDto requestDto
	) {
		Long userId = (Long)request.getSession().getAttribute("userId");

		OrderResponseDto responseDto = orderService.updateOrder(userId, orderId, requestDto);

		return ResponseEntity.status(HttpStatus.OK).body(responseDto);
	}

	@DeleteMapping("/{orderId}/refuse")
	public ResponseEntity<Void> refuseOrder(
		HttpServletRequest request,
		@PathVariable Long orderId
	) {
		Long userId = (Long)request.getSession().getAttribute("userId");

		orderService.refuseOrder(userId, orderId);

		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@GetMapping
	public ResponseEntity<List<OrderResponseDto>> findAllOrders(
		HttpServletRequest request
	) {
		Long userId = (Long)request.getSession().getAttribute("userId");

		List<OrderResponseDto> responseDtos = orderService.findAllOrders(userId);

		return ResponseEntity.status(HttpStatus.OK).body(responseDtos);
	}

	@GetMapping("/{orderId}/details")
	public ResponseEntity<OrderDetailResponseDto> findOrderDetails(
		HttpServletRequest request,
		@PathVariable Long orderId
	) {
		Long userId = (Long)request.getSession().getAttribute("userId");

		OrderDetailResponseDto responseDto = orderService.findOrderDetails(userId, orderId);

		return ResponseEntity.status(HttpStatus.OK).body(responseDto);
	}

}
