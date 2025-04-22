package com.example.dishpatch.infra.db.statistics.repository;

import com.example.dishpatch.infra.db.statistics.entity.OrderStatId;
import com.example.dishpatch.infra.db.statistics.entity.OrderStatMonthly;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreOrderStatMonthlyRepository
        extends JpaRepository<OrderStatMonthly, OrderStatId> {

}
