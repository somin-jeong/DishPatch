package com.example.dishpatch.infra.db.user.entity;

public enum UserGrade {
	A("천생연분"), B("더귀한분"), C("귀한분"), D("고마운분");

	private final String description;

	UserGrade(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

}
