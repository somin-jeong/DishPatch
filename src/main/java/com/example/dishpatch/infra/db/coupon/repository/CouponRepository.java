package com.example.dishpatch.infra.db.coupon.repository;

import com.example.dishpatch.infra.db.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
