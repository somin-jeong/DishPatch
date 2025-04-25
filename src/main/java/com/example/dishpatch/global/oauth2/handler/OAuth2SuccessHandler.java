package com.example.dishpatch.global.oauth2.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.dishpatch.domain.user.exception.UserErrorCode;
import com.example.dishpatch.global.exception.BizException;
import com.example.dishpatch.global.security.JwtUtil;
import com.example.dishpatch.infra.db.user.entity.User;
import com.example.dishpatch.infra.db.user.entity.UserProvider;
import com.example.dishpatch.infra.db.user.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

	private final JwtUtil jwtUtil;
	private final UserRepository userRepository;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
		String email = (String) oAuth2User.getAttributes().get("email");

		User user = userRepository.findByEmailAndProvider(email, UserProvider.NAVER)
			.orElseThrow(() -> new BizException(UserErrorCode.INVALID_ID));

		String token = jwtUtil.createToken(user.getId(),user.getRole());

		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write("{\"token\":\""+token+"\"}");
		response.sendRedirect("http://localhost:3000/oauth/success?token=" + token);
	}
}
