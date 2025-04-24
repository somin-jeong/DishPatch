package com.example.dishpatch.infra.db.admin.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dishpatch.infra.db.admin.entity.AdminStoreOrderStatMonthly;

public interface AdminStoreOrderStatMonthlyRepository
	extends JpaRepository<AdminStoreOrderStatMonthly, LocalDate>, AdminStoreOrderStatMonthlyQueryRepository {

}
