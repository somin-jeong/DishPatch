package com.example.dishpatch.infra.db.order.entity;

public enum OrderStatus {
	A("확인중"),
	B("조리중"),
	C("배달중"),
	D("배달완료");

	private final String description;

	OrderStatus(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
