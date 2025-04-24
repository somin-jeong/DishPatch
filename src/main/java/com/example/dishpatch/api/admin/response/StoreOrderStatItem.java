package com.example.dishpatch.api.admin.response;

import java.time.LocalDate;

public record StoreOrderStatItem(
	LocalDate date,
	Integer orderCount,
	Long totalSales
) {

}
