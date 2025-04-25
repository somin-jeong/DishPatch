package com.example.dishpatch.api.order.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import com.example.dishpatch.global.security.UserAuth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@PostMapping
	public ResponseEntity<OrderResponseDto> createOrder(
		@AuthenticationPrincipal UserAuth userAuth,
		@Valid @RequestBody OrderRequestDto requestDto
	) {
		OrderResponseDto responseDto = orderService.createOrder(requestDto, userAuth);

		return ResponseEntity.status(HttpStatus.OK).body(responseDto);
	}

	@PatchMapping("/{orderId}/update")
	public ResponseEntity<OrderResponseDto> updateOrder(
		@AuthenticationPrincipal UserAuth userAuth,
		@PathVariable Long orderId,
		@Valid @RequestBody OrderStatusRequestDto requestDto
	) {
		OrderResponseDto responseDto = orderService.updateOrder(userAuth.getId(), orderId, requestDto);

		return ResponseEntity.status(HttpStatus.OK).body(responseDto);
	}

	@DeleteMapping("/{orderId}/refuse")
	public ResponseEntity<Void> refuseOrder(
		@AuthenticationPrincipal UserAuth userAuth,
		@PathVariable Long orderId
	) {
		orderService.refuseOrder(userAuth.getId(), orderId);

		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@GetMapping
	public ResponseEntity<List<OrderResponseDto>> findAllOrders(
		@AuthenticationPrincipal UserAuth userAuth
	) {
		List<OrderResponseDto> responseDtos = orderService.findAllOrders(userAuth.getId());

		return ResponseEntity.status(HttpStatus.OK).body(responseDtos);
	}

	@GetMapping("/{orderId}/details")
	public ResponseEntity<OrderDetailResponseDto> findOrderDetails(
		@AuthenticationPrincipal UserAuth userAuth,
		@PathVariable Long orderId
	) {
		OrderDetailResponseDto responseDto = orderService.findOrderDetails(userAuth.getId(), orderId);

		return ResponseEntity.status(HttpStatus.OK).body(responseDto);
	}

}
