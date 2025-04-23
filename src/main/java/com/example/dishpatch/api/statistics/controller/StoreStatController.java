package com.example.dishpatch.api.statistics.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dishpatch.api.statistics.request.StoreOrderStatRequest;
import com.example.dishpatch.api.statistics.response.StoreOrderStatResponse;
import com.example.dishpatch.domain.statistics.service.StoreOrderStatService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/stores/{storeId}/stats")
public class StoreStatController {

	private final StoreOrderStatService storeOrderStatService;

	@GetMapping("/orders")
	public ResponseEntity<StoreOrderStatResponse> getOrderStatistics(
		@PathVariable("storeId") Long storeId,
		@Valid @ModelAttribute StoreOrderStatRequest req
	) {
		StoreOrderStatResponse res = storeOrderStatService.getOrderStatistics(storeId, req);
		return ResponseEntity.ok(res);
	}

}
