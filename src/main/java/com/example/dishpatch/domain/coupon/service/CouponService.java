package com.example.dishpatch.domain.coupon.service;

import org.springframework.stereotype.Service;

import com.example.dishpatch.infra.db.coupon.entity.Coupon;
import com.example.dishpatch.infra.db.coupon.entity.CouponUsed;
import com.example.dishpatch.infra.db.coupon.repository.CouponRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponService {

	private final CouponRepository couponRepository;

	public Coupon getCoupon(Long couponId) {

		Coupon coupon = couponRepository.findById(couponId)
			.orElseThrow(() -> new RuntimeException("쿠폰이 존재하지 않습니다.")
			);

		if (coupon.getStatus() == CouponUsed.A) {
			throw new RuntimeException("이미 만료된 쿠폰입니다.");
		}
		return coupon;
	}

	@Transactional
	public void useCoupon(Coupon coupon) {
		coupon.useCoupon();
	}
}
