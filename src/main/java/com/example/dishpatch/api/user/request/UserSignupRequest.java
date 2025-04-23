package com.example.dishpatch.api.user.request;

import com.example.dishpatch.infra.db.user.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserSignupRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "비밀번호는 8자 이상이며, 대문자, 소문자, 숫자, 특수문자를 모두 포함해야 합니다."
    )
    private String password;

    @NotBlank
    @Min(value = 2)
    private String name;

    @NotBlank
    @Pattern(regexp = "^\\d{11}$", message = "휴대폰 번호는 숫자 11자리여야 합니다.")
    private String phone;

    @NotBlank
    private String currentAddress;

    @NotBlank
    private UserRole role;


}
