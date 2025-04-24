package com.example.dishpatch.api.menu.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.example.dishpatch.global.security.UserAuth;

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
		@AuthenticationPrincipal UserAuth userAuth,
		@Valid @RequestBody MenuOptionAddRequest req
	) {
		MenuOptionAddResponse res = menuOptionService.addMenuOption(userAuth.getId(), menuId, req);
		return ResponseEntity.status(HttpStatus.CREATED).body(res);
	}

	@PutMapping("/{optionId}")
	public ResponseEntity<Void> updateMenuOption(
		@PathVariable Long menuId,
		@PathVariable Long optionId,
		@AuthenticationPrincipal UserAuth userAuth,
		@Valid @RequestBody MenuOptionUpdateRequest req
	) {
		menuOptionService.updateMenuOption(userAuth.getId(), menuId, optionId, req);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{optionId}")
	public ResponseEntity<Void> deleteMenuOption(
		@PathVariable Long menuId,
		@PathVariable Long optionId,
		@AuthenticationPrincipal UserAuth userAuth
	) {
		menuOptionService.deleteMenuOption(userAuth.getId(), menuId, optionId);
		return ResponseEntity.noContent().build();
	}
}
