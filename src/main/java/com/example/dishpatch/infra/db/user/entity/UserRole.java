package com.example.dishpatch.infra.db.user.entity;

public enum UserRole {
    USER("고객"), CEO("사장"), ADMIN("관리자");

    private final String description;


    UserRole(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
