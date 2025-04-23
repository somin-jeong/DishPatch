package com.example.dishpatch.infra.db.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dishpatch.infra.db.cart.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
