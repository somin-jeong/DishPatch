package com.example.dishpatch.infra.db.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dishpatch.infra.db.order.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
