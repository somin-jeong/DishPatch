package com.example.dishpatch.domain.coupon.service;

import org.springframework.stereotype.Service;

import com.example.dishpatch.domain.coupon.exception.CouponErrorCode;
import com.example.dishpatch.global.exception.BizException;
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
			.orElseThrow(() -> new BizException(CouponErrorCode.COUPON_NOT_EXIST)
			);

		if (coupon.getStatus() == CouponUsed.A) {
			throw new BizException(CouponErrorCode.COUPON_EXPIRED);
		}
		return coupon;
	}

	@Transactional
	public void useCoupon(Coupon coupon) {
		Coupon savedCoupon = coupon;
		savedCoupon.useCoupon();
	}
}
