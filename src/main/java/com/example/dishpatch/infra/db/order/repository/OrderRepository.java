package com.example.dishpatch.infra.db.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.dishpatch.infra.db.order.entity.Order;
import com.example.dishpatch.infra.db.user.entity.User;

public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findByUser(User user);

	@Query("SELECT o FROM Order o WHERE o.store.id IN :storeIds")
	List<Order> findByStoreIds(@Param("storeIds") List<Long> storeIds);

}
