package com.example.dishpatch.infra.db.pointHistory.entity;

public enum PointUsed {
	USED("Used"), UNUSED("Unused");

	private final String value;

	PointUsed(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
