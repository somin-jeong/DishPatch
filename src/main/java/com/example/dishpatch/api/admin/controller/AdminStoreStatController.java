package com.example.dishpatch.api.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dishpatch.api.admin.request.StoreOrderStatRequest;
import com.example.dishpatch.api.admin.response.StoreOrderStatResponse;
import com.example.dishpatch.domain.admin.service.AdminStoreStatService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/stats/stores")
public class AdminStoreStatController {

	private final AdminStoreStatService storeStatService;

	@GetMapping("/orders")
	public ResponseEntity<StoreOrderStatResponse> getOrderStat(
		@Valid @ModelAttribute StoreOrderStatRequest req
	) {
		StoreOrderStatResponse res = storeStatService.getOrderStat(req);
		return ResponseEntity.ok(res);
	}
}
