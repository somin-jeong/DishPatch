package com.example.dishpatch.api.menu.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dishpatch.api.menu.request.MenuCreateRequest;
import com.example.dishpatch.api.menu.request.MenuUpdateRequest;
import com.example.dishpatch.api.menu.response.MenuCreateResponse;
import com.example.dishpatch.api.menu.response.StoreMenuListResponse;
import com.example.dishpatch.domain.menu.service.MenuService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/stores/{storeId}/menus")
public class MenuController {

	private final MenuService menuService;

	@PostMapping
	public ResponseEntity<MenuCreateResponse> createMenu(
		@PathVariable("storeId") Long storeId,
		@AuthenticationPrincipal Long userId,
		@Valid @RequestBody MenuCreateRequest req
	) {
		MenuCreateResponse res = menuService.createMenu(userId, storeId, req);
		return ResponseEntity.status(HttpStatus.CREATED).body(res);
	}

	@GetMapping
	public ResponseEntity<StoreMenuListResponse> getStoreMenus(
		@PathVariable("storeId") Long storeId
	) {
		StoreMenuListResponse res = menuService.getStoreMenus(storeId);
		return ResponseEntity.ok(res);
	}

	@PutMapping("/{menuId}")
	public ResponseEntity<Void> updateMenu(
		@PathVariable("storeId") Long storeId,
		@PathVariable("menuId") Long menuId,
		@AuthenticationPrincipal Long userId,
		@Valid @RequestBody MenuUpdateRequest req
	) {
		menuService.updateMenu(userId, storeId, menuId, req);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{menuId}")
	public ResponseEntity<Void> deleteMenu(
		@PathVariable("storeId") Long storeId,
		@PathVariable("menuId") Long menuId,
		@AuthenticationPrincipal Long userId
	) {
		menuService.deleteMenu(userId, storeId, menuId);
		return ResponseEntity.noContent().build();
	}
}
