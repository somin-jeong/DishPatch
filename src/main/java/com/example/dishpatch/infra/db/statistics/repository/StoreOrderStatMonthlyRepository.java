package com.example.dishpatch.infra.db.statistics.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dishpatch.infra.db.statistics.entity.OrderStatId;
import com.example.dishpatch.infra.db.statistics.entity.OrderStatMonthly;

public interface StoreOrderStatMonthlyRepository
	extends JpaRepository<OrderStatMonthly, OrderStatId> {

}
