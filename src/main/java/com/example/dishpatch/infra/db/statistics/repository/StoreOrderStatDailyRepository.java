package com.example.dishpatch.infra.db.statistics.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dishpatch.infra.db.statistics.entity.OrderStatDaily;
import com.example.dishpatch.infra.db.statistics.entity.OrderStatId;

public interface StoreOrderStatDailyRepository
	extends JpaRepository<OrderStatDaily, OrderStatId> {

}
