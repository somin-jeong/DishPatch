package com.example.dishpatch.api.store.request;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StoreCreateRequest(
	@NotBlank
	String name,
	@NotBlank
	String address,
	String imageUrl,
	@NotBlank
	String phone,
	@NotNull
	int deliveryFee,
	String introduction,
	@NotNull
	int minDeliveryPrice,
	@NotNull
	boolean isAdvertised,
	@NotBlank
	String openTime,
	@NotBlank
	String closeTime,
	@NotNull
	Long categoryId
) {
	public LocalTime getCloseTime() {
		return LocalTime.parse(closeTime, DateTimeFormatter.ofPattern("HH:mm"));
	}

	public LocalTime getOpenTime() {
		return LocalTime.parse(openTime, DateTimeFormatter.ofPattern("HH:mm"));
	}
}
