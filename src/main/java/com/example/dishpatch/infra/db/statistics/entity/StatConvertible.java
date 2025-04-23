package com.example.dishpatch.infra.db.statistics.entity;

import java.time.LocalDate;

public interface StatConvertible {
	LocalDate getDate();

	Integer getOrderCount();

	Long getTotalSales();
}
