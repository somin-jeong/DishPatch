package com.example.dishpatch.infra.db.store.entity;

public enum StoreStatus {
    ACTIVE("가게 활성화"),UNACTIVE("가게 비활성화");

    private final String status;

    StoreStatus(String status) {this.status = status;}

    public String getStatus() {
        return status;
    }
}
