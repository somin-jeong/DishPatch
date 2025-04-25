package com.example.dishpatch.infra.db.coupon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.dishpatch.infra.db.coupon.entity.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

	@Modifying(clearAutomatically = true)
	@Query("UPDATE Coupon c SET c.deletedDate = CURRENT_TIMESTAMP WHERE c.user.id = :userId")
	void deleteByUserId(@Param("userId") Long userId);
}
