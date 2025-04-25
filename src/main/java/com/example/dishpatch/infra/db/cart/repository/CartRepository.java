package com.example.dishpatch.infra.db.cart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.example.dishpatch.infra.db.cart.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
	@Modifying(clearAutomatically = true)
	void deleteAllByStoreId(Long storeId);

	List<Cart> findByUserId(Long userId);

	@Modifying(clearAutomatically = true)
	void deleteAllById(Long cartId);
}
