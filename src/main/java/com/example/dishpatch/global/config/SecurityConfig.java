package com.example.dishpatch.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final SecurityFilter securityFilter;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
			.sessionManagement(session
				-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/users/signup", "/users/login")
				.permitAll()
				.requestMatchers("/admin/**")
				.hasRole("ADMIN")
				.requestMatchers("/stores/{storeId}/menus", "/stores/{storeId}/menus/{menuId}",
					"/stores/{storeId}/menus/{menuId}", "/menus/{menuId}/options", "/menus/{menuId}/options/{optionId}",
					"/stores/{storeId}/stats/daily", "/stores/{storeId}/stats/monthly", "/orders/{orderId}/refuse",
					"/stores", "/stores/{storeId}", "/orders/{orderId}/update")
				.hasRole("CEO")
				.requestMatchers(HttpMethod.POST, "/ceoReviews/{reviewId}")
				.hasRole("CEO")
				.requestMatchers(HttpMethod.PATCH, "/ceoReviews/{ceoReviewId}")
				.hasRole("CEO")
				.requestMatchers(HttpMethod.DELETE, "/reviews/{reviewId}")
				.hasAnyRole("USER", "ADMIN")
				.requestMatchers(HttpMethod.DELETE, "/ceoReviews/{ceoReviewId}", "/stores/{storeId}")
				.hasAnyRole("CEO", "ADMIN")
				.anyRequest()
				.authenticated()
			)
			.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
