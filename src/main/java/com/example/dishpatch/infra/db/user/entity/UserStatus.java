package com.example.dishpatch.infra.db.user.entity;

public enum UserStatus {

    ACTIVE("계정 활성화"),UNACTIVE("계정 비활성화");

    private final String description;

    UserStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
