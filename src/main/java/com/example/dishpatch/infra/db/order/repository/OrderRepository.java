package com.example.dishpatch.infra.db.order.repository;

import com.example.dishpatch.infra.db.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
