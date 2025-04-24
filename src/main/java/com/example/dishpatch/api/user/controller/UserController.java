package com.example.dishpatch.api.user.controller;

import com.example.dishpatch.api.user.request.UserLoginRequest;
import com.example.dishpatch.api.user.request.UserSignupRequest;
import com.example.dishpatch.api.user.response.UserLoginResponse;
import com.example.dishpatch.api.user.response.UserSignupResponse;
import com.example.dishpatch.domain.user.service.UserService;
import com.example.dishpatch.global.config.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<UserSignupResponse> signUp(@Valid @RequestBody UserSignupRequest request) {

        UserSignupResponse userSignupResponse = userService.signUp(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(userSignupResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody UserLoginRequest request) {

        UserLoginResponse login = userService.login(request);

        return ResponseEntity.status(HttpStatus.OK).body(login);
    }

    @PostMapping("/logout/{id}")
    public ResponseEntity<String> logout(HttpServletRequest request) {

        String token = jwtUtil.extractToken(request);
        long expiration = jwtUtil.getExpiration(token);


        return ResponseEntity.status(HttpStatus.OK).body("로그아웃 완료");
    }

}
