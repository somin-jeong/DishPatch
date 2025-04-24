package com.example.dishpatch.api.menu.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dishpatch.api.menu.request.MenuOptionAddRequest;
import com.example.dishpatch.api.menu.request.MenuOptionUpdateRequest;
import com.example.dishpatch.api.menu.response.MenuOptionAddResponse;
import com.example.dishpatch.domain.menu.service.MenuOptionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/menus/{menuId}/options")
public class MenuOptionController {

	private final MenuOptionService menuOptionService;

	@PostMapping
	public ResponseEntity<MenuOptionAddResponse> addMenuOption(
		@PathVariable Long menuId,
		@AuthenticationPrincipal Long userId,
		@Valid @RequestBody MenuOptionAddRequest req
	) {
		MenuOptionAddResponse res = menuOptionService.addMenuOption(userId, menuId, req);
		return ResponseEntity.status(HttpStatus.CREATED).body(res);
	}

	@PutMapping("/{optionId}")
	public ResponseEntity<Void> updateMenuOption(
		@PathVariable Long menuId,
		@PathVariable Long optionId,
		@AuthenticationPrincipal Long userId,
		@Valid @RequestBody MenuOptionUpdateRequest req
	) {
		menuOptionService.updateMenuOption(userId, menuId, optionId, req);
		return ResponseEntity.noContent().build();
	}
}
