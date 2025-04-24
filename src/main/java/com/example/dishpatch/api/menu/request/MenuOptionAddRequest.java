package com.example.dishpatch.api.menu.request;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MenuOptionAddRequest(
	@NotBlank
	@Length(min = 1, max = 20)
	String name,

	@NotNull
	Integer price
) {
}
