package com.example.dishpatch.api.cart.request;

public record CartCreateRequest(
	Long menuId,
	Long menuOptionId,
	int quantity
) {
}
