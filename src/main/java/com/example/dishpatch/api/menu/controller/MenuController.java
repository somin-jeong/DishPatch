package com.example.dishpatch.api.menu.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dishpatch.api.menu.request.MenuCreateRequest;
import com.example.dishpatch.api.menu.response.MenuCreateResponse;
import com.example.dishpatch.domain.menu.service.MenuService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/stores/{storeId}")
public class MenuController {

	private final MenuService menuService;

	@PostMapping("/menus")
	public ResponseEntity<MenuCreateResponse> createMenu(
		@PathVariable("storeId") Long storeId,
		@Valid @RequestBody MenuCreateRequest req
	) {
		MenuCreateResponse res = menuService.createMenu(storeId, req);
		return ResponseEntity.status(HttpStatus.CREATED).body(res);
	}
}
