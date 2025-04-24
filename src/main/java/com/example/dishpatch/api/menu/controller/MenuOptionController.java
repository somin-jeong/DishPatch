package com.example.dishpatch.api.menu.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dishpatch.api.menu.request.MenuAddRequest;
import com.example.dishpatch.api.menu.response.MenuAddResponse;
import com.example.dishpatch.domain.menu.service.MenuOptionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/menus/{menuId}/options")
public class MenuOptionController {

	private final MenuOptionService menuOptionService;

	@PostMapping
	public ResponseEntity<MenuAddResponse> addMenuOption(
		@PathVariable Long menuId,
		@AuthenticationPrincipal Long userId,
		@Valid @RequestBody MenuAddRequest req
	) {
		MenuAddResponse res = menuOptionService.addMenuOption(userId, menuId, req);
		return ResponseEntity.status(HttpStatus.CREATED).body(res);
	}
}
