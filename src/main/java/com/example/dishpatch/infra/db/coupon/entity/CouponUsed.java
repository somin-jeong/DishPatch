package com.example.dishpatch.infra.db.coupon.entity;

public enum CouponUsed {
	A("Used"), B("Unused");

	private final String value;

	CouponUsed(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
