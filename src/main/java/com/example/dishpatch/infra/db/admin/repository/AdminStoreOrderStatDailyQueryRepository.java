package com.example.dishpatch.infra.db.admin.repository;

import java.time.LocalDate;
import java.util.List;

import com.example.dishpatch.infra.db.admin.entity.AdminStoreOrderStatDaily;

public interface AdminStoreOrderStatDailyQueryRepository {
	List<AdminStoreOrderStatDaily> findAllByDateRange(LocalDate from, LocalDate to);
}
