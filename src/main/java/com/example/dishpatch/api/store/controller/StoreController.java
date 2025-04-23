package com.example.dishpatch.api.store.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

	@PostMapping("/{storeId}")
	public void dibStore(
		// TODO: authentication user
		@PathVariable("storeId") Long storeId
	) {
		storeService.dibStore(new User(), storeId);
	}

	@DeleteMapping("/{storeId}")
	public void undibStore(
		// TODO: authentication user
		@PathVariable("storeId") Long storeId
	) {
		storeService.undibStore(new User(), storeId);
	}
}
