package com.example.dishpatch.global.oauth2.service;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.dishpatch.infra.db.user.entity.User;
import com.example.dishpatch.infra.db.user.entity.UserGrade;
import com.example.dishpatch.infra.db.user.entity.UserProvider;
import com.example.dishpatch.infra.db.user.entity.UserRole;
import com.example.dishpatch.infra.db.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final UserRepository userRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);

		Map<String, Object> attributes = oAuth2User.getAttributes();
		Map<String, Object> response = (Map<String, Object>) attributes.get("response");

		String email = (String) response.get("email");
		String name = (String) response.get("name");
		String mobile = (String) response.get("mobile");
		String providerId = (String) response.get("id");
		String provider = userRequest.getClientRegistration().getRegistrationId();

		userRepository.findByEmailAndProvider(email, UserProvider.valueOf(provider.toUpperCase()))
			.orElseGet(() -> {
				String dummyPassword = UUID.randomUUID().toString();

				User newUser = new User(
					email,
					dummyPassword,
					mobile,
					name,
					UserProvider.valueOf(provider.toUpperCase()),
					UserGrade.D,
					UserRole.USER,
					"미입력"
				);

				newUser.setProviderId(providerId);
				return userRepository.save(newUser);
			});

		return new DefaultOAuth2User(
			Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
			response,"email");
	}
}
