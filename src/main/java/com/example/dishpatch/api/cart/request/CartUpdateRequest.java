package com.example.dishpatch.api.cart.request;

public record CartUpdateRequest(
	Long menuId,
	Long menuOptionId,
	int quantity
) {
}
