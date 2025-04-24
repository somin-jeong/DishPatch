package com.example.dishpatch.api.admin.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public record StoreOrderStatRequest(

	@NotNull
	LocalDate from,

	@NotNull
	LocalDate to,

	@NotNull
	StoreOrderStatPeriodType period
) {
}
