package com.example.dishpatch.infra.db.store.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dishpatch.infra.db.store.entity.Dib;

public interface DibRepository extends JpaRepository<Dib, Long> {
	boolean existsByUserIdAndStoreId(Long userId, Long storeId);

	Optional<Dib> findByUserIdAndStoreId(Long id, Long storeId);
}
