package com.example.dishpatch.infra.db.statistics.repository;

import com.example.dishpatch.infra.db.statistics.entity.OrderStatDaily;
import com.example.dishpatch.infra.db.statistics.entity.OrderStatId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreOrderStatDailyRepository
        extends JpaRepository<OrderStatDaily, OrderStatId> {

}
