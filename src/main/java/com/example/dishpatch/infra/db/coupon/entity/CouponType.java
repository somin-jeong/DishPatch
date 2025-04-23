package com.example.dishpatch.infra.db.coupon.entity;

public enum CouponType {
	A("퍼센티지 할인 쿠폰"), B("금액 할인 쿠폰");

	private final String description;

	CouponType(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
