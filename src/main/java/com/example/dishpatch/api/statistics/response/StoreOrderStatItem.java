package com.example.dishpatch.api.statistics.response;

import java.time.LocalDate;

public record StoreOrderStatItem(
	LocalDate date,
	Integer orderCount,
	Long totalSales
) {

}
