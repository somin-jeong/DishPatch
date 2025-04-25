package com.example.dishpatch.api.cart.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dishpatch.api.cart.request.CartCreateRequest;
import com.example.dishpatch.api.cart.response.CartCreateResponse;
import com.example.dishpatch.api.cart.response.CartResponseDto;
import com.example.dishpatch.domain.cart.service.CartService;
import com.example.dishpatch.global.security.UserAuth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/store/{storeId}")
@RequiredArgsConstructor
public class CartController {

	private final CartService cartService;

	@PostMapping("/cart")
	public ResponseEntity<CartCreateResponse> createCart(
		@PathVariable Long storeId,
		@Valid @RequestBody CartCreateRequest request,
		@AuthenticationPrincipal UserAuth userAuth
	) {
		CartCreateResponse response = cartService.createCart(storeId, request, userAuth);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/carts")
	public ResponseEntity<CartResponseDto> findCarts(
		@PathVariable Long storeId,
		@AuthenticationPrincipal UserAuth userAuth
	) {
		CartResponseDto response = cartService.findCarts(storeId, userAuth);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
