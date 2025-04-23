package com.example.dishpatch.domain.user.service;

import com.example.dishpatch.api.user.request.UserLoginRequest;
import com.example.dishpatch.api.user.request.UserSignupRequest;
import com.example.dishpatch.api.user.response.UserLoginResponse;
import com.example.dishpatch.api.user.response.UserSignupResponse;
import com.example.dishpatch.domain.user.exception.UserErrorCode;
import com.example.dishpatch.global.config.JwtUtil;
import com.example.dishpatch.global.config.SecurityConfig;
import com.example.dishpatch.global.exception.BizException;
import com.example.dishpatch.infra.db.user.entity.User;
import com.example.dishpatch.infra.db.user.entity.UserGrade;
import com.example.dishpatch.infra.db.user.entity.UserProvider;
import com.example.dishpatch.infra.db.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final SecurityConfig securityConfig;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public UserSignupResponse signUp(UserSignupRequest request) {

        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new BizException(UserErrorCode.INVALID_EMAIL);
        }

        String encodedPassword = passwordEncoder.encode(request.password());

        User user = new User(request.email(), encodedPassword, request.phone(), request.name(), UserProvider.LOCAL, UserGrade.D, request.role(), request.currentAddress());

        User save = userRepository.save(user);

        return UserSignupResponse.from(save);
    }

    @Override
    public UserLoginResponse login(UserLoginRequest request) {

        User user = userRepository.findByEmail(request.email()).orElseThrow(
                () -> new BizException(UserErrorCode.INVALID_EMAIL));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BizException(UserErrorCode.INVALID_PASSWORD);
        }

        String token = jwtUtil.createToken(request.email());

        return new UserLoginResponse(token);
    }
}
