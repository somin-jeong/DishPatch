package com.example.dishpatch.infra.db.order.entity;

public enum OrderStatus {
	CHECKING("확인중"),
	COOKING("조리중"),
	DELIVERING("배달중"),
	FINISHED("배달완료");

	private final String description;

	OrderStatus(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
