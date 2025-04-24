package com.example.dishpatch.api.user.request;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserUpdateRequest(
	@NotBlank
	@Pattern(
		regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
		message = "비밀번호는 8자 이상이며, 대문자, 소문자, 숫자, 특수문자를 모두 포함해야 합니다."
	)
	String password,

	@NotBlank
	@Length(min = 2)
	String name,

	@NotBlank
	@Pattern(regexp = "^\\d{11}$", message = "휴대폰 번호는 숫자 11자리여야 합니다.")
	String phone,

	@NotBlank
	String currentAddress
) {
}
