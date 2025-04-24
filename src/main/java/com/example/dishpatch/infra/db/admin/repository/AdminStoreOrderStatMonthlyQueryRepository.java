package com.example.dishpatch.infra.db.admin.repository;

import java.time.LocalDate;
import java.util.List;

import com.example.dishpatch.infra.db.admin.entity.AdminStoreOrderStatMonthly;

public interface AdminStoreOrderStatMonthlyQueryRepository {
	List<AdminStoreOrderStatMonthly> findAllByDateRange(LocalDate from, LocalDate to);
}
