package com.example.dishpatch.infra.db.coupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dishpatch.infra.db.coupon.entity.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
