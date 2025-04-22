package com.example.dishpatch.infra.db.order.repository;

import com.example.dishpatch.infra.db.order.entity.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemsRepository extends JpaRepository<OrderItems, Integer> {
}
