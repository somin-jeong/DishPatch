package com.example.dishpatch.domain.user.service;

import com.example.dishpatch.api.user.request.UserLoginRequest;
import com.example.dishpatch.api.user.request.UserSignupRequest;
import com.example.dishpatch.api.user.response.UserLoginResponse;
import com.example.dishpatch.api.user.response.UserSignupResponse;
import com.example.dishpatch.global.config.JwtUtil;
import com.example.dishpatch.global.config.SecurityConfig;
import com.example.dishpatch.infra.db.user.entity.User;
import com.example.dishpatch.infra.db.user.entity.UserGrade;
import com.example.dishpatch.infra.db.user.entity.UserProvider;
import com.example.dishpatch.infra.db.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 사용중인 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.password());

        User user = new User(request.email(), encodedPassword, request.phone(), request.name(), UserProvider.LOCAL, UserGrade.D, request.role(), request.currentAddress());

        User save = userRepository.save(user);

        return UserSignupResponse.from(save);
    }

    @Override
    public UserLoginResponse login(UserLoginRequest request) {

        User user = userRepository.findByEmail(request.email()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유효하지 않은 이메일입니다."));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 틀렸습니다.");
        }

        String token = jwtUtil.createToken(request.email());

        return new UserLoginResponse(token);
    }
}
