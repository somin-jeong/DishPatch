package com.example.dishpatch.infra.db.store.repository;

import com.example.dishpatch.infra.db.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
	int countByUserIdAndDeletedDateIsNull(Long userId);
}
