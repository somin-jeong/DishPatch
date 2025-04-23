package com.example.dishpatch.infra.db.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dishpatch.infra.db.store.entity.Store;

public interface StoreRepository extends JpaRepository<Store, Integer> {
}
