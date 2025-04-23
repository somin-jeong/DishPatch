package com.example.dishpatch.infra.db.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dishpatch.infra.db.store.entity.Store;
import com.example.dishpatch.infra.db.user.entity.User;

public interface StoreRepository extends JpaRepository<Store, Long> {
	int countByUser(User user);
}
