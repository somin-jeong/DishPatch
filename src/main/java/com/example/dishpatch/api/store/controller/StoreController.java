package com.example.dishpatch.api.store.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dishpatch.api.store.request.StoreCreateRequest;
import com.example.dishpatch.api.store.response.StoreCreateResponse;
import com.example.dishpatch.domain.store.service.StoreService;
import com.example.dishpatch.infra.db.user.entity.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/stores")
public class StoreController {
	private final StoreService storeService;

	@PostMapping
	public ResponseEntity<StoreCreateResponse> createStore(
		// TODO: authentication user
		@Valid @RequestBody StoreCreateRequest request
	) {
		return ResponseEntity.status(HttpStatus.CREATED).body(storeService.createStore(new User(), request));
	}
}
