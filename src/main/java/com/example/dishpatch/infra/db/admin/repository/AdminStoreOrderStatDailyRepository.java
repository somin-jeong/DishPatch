package com.example.dishpatch.infra.db.admin.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dishpatch.infra.db.admin.entity.AdminStoreOrderStatDaily;

public interface AdminStoreOrderStatDailyRepository
	extends JpaRepository<AdminStoreOrderStatDaily, LocalDate>, AdminStoreOrderStatDailyQueryRepository {

}
