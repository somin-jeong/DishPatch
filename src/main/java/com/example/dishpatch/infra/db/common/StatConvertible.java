package com.example.dishpatch.infra.db.common;

import java.time.LocalDate;

public interface StatConvertible {
	LocalDate getDate();

	Integer getOrderCount();

	Long getTotalSales();
}
