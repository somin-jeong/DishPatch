package com.example.dishpatch.infra.db.cart.repository;

import com.example.dishpatch.infra.db.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
