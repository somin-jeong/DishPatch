package com.example.dishpatch.infra.db.store.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dishpatch.infra.db.store.entity.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {
	int countByUserIdAndDeletedDateIsNull(Long userId);

	Optional<Store> findByIdAndDeletedDateIsNull(Long storeId);

	boolean existsByIdAndDeletedDateIsNull(Long storeId);
}
