package com.example.dishpatch.infra.db.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.dishpatch.infra.db.order.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

	@Query("SELECT oi.id FROM OrderItem oi WHERE oi.order.id = :orderId")
	List<Long> findIdsByOrderId(@Param("orderId") Long orderId);
}
