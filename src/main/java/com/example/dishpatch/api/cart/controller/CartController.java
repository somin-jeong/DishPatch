package com.example.dishpatch.api.cart.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dishpatch.api.cart.request.CartCreateRequest;
import com.example.dishpatch.api.cart.request.CartUpdateRequest;
import com.example.dishpatch.api.cart.response.CartCreateResponse;
import com.example.dishpatch.api.cart.response.CartResponseDto;
import com.example.dishpatch.domain.cart.service.CartService;
import com.example.dishpatch.global.security.UserAuth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

	private final CartService cartService;

	@PostMapping
	public ResponseEntity<CartCreateResponse> createCart(
		@Valid @RequestBody CartCreateRequest request,
		@AuthenticationPrincipal UserAuth userAuth
	) {
		CartCreateResponse response = cartService.createCart(request, userAuth);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping
	public ResponseEntity<CartResponseDto> findCarts(
		@AuthenticationPrincipal UserAuth userAuth
	) {
		CartResponseDto response = cartService.findCarts(userAuth);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PatchMapping("/{cartId}")
	public ResponseEntity<CartResponseDto> updateCart(
		@PathVariable Long cartId,
		@Valid @RequestBody CartUpdateRequest request,
		@AuthenticationPrincipal UserAuth userAuth
	) {
		CartResponseDto response = cartService.updateCart(cartId, request, userAuth);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
