package com.example.dishpatch.api.store.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dishpatch.api.store.request.StoreCreateRequest;
import com.example.dishpatch.api.store.request.StoreUpdateRequest;
import com.example.dishpatch.api.store.response.StoreCreateResponse;
import com.example.dishpatch.api.store.response.StoreInfoResponse;
import com.example.dishpatch.api.store.response.StoreResponse;
import com.example.dishpatch.api.store.response.StoreSearchResponse;
import com.example.dishpatch.domain.store.service.StoreService;
import com.example.dishpatch.global.response.pagination.SliceResponse;
import com.example.dishpatch.global.security.UserAuth;
import com.example.dishpatch.infra.db.store.enums.SortType;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/stores")
public class StoreController {
	private final StoreService storeService;

	@PostMapping
	public ResponseEntity<StoreCreateResponse> createStore(
		@AuthenticationPrincipal UserAuth userAuth,
		@Valid @RequestBody StoreCreateRequest request
	) {
		return ResponseEntity.status(HttpStatus.CREATED).body(storeService.createStore(userAuth, request));
	}

	@PostMapping("/{storeId}/like")
	public void dibStore(
		@AuthenticationPrincipal UserAuth userAuth,
		@PathVariable("storeId") Long storeId
	) {
		storeService.dibStore(userAuth, storeId);
	}

	@DeleteMapping("/{storeId}/like")
	public void undibStore(
		@AuthenticationPrincipal UserAuth userAuth,
		@PathVariable("storeId") Long storeId
	) {
		storeService.undibStore(userAuth, storeId);
	}

	@PutMapping("/{storeId}")
	public void updateStore(
		@AuthenticationPrincipal UserAuth userAuth,
		@PathVariable("storeId") Long storeId,
		@Valid @RequestBody StoreUpdateRequest request
	) {
		storeService.updateStore(userAuth, storeId, request);
	}

	@DeleteMapping("/{storeId}")
	public void deleteStore(
		@AuthenticationPrincipal UserAuth userAuth,
		@PathVariable("storeId") Long storeId
	) {
		storeService.deleteStore(userAuth, storeId);
	}

	@GetMapping
	public ResponseEntity<SliceResponse<StoreResponse>> getStore(
		@RequestParam(required = false) SortType sortType,  // dib, rating, orderCount
		@RequestParam(required = false) Long categoryId,
		@RequestParam(required = false) Long cursorId,
		@RequestParam(defaultValue = "10") int size
	) {
		return ResponseEntity.ok(storeService.getStore(sortType, categoryId, cursorId, size));
	}

	@GetMapping("/{storeId}")
	public ResponseEntity<StoreInfoResponse> getStoreInfo(
		@PathVariable("storeId") Long storeId
	) {
		return ResponseEntity.ok(storeService.getStoreInfo(storeId));
	}

	@GetMapping("/search")
	public ResponseEntity<SliceResponse<StoreSearchResponse>> searchStore(
		@RequestParam String keyword,
		@RequestParam(required = false) Long cursorId,
		@RequestParam(defaultValue = "10") int size
	) {
		return ResponseEntity.ok(storeService.searchStore(keyword, cursorId, size));
	}

}
