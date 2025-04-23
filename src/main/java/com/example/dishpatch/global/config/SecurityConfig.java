package com.example.dishpatch.global.config;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder() {
			@Override
			public String encode(CharSequence rawPassword) {
				return BCrypt.withDefaults()
						.hashToString(BCrypt.MIN_COST,rawPassword.toString().toCharArray());
			}

			@Override
			public boolean matches(CharSequence rawPassword, String encodedPassword) {
				return BCrypt.verifyer()
						.verify(rawPassword.toString().toCharArray(),encodedPassword)
						.verified;
			}
		};
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.build();
	}

}
