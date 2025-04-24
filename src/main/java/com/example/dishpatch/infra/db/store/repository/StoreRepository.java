package com.example.dishpatch.infra.db.store.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.dishpatch.infra.db.store.entity.Store;
import com.example.dishpatch.infra.db.user.entity.User;

import com.example.dishpatch.infra.db.store.entity.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {
	int countByUserIdAndDeletedDateIsNull(Long userId);
  
	Optional<Store> findByIdAndDeletedDateIsNull(Long storeId);

	boolean existsByIdAndDeletedDateIsNull(Long storeId);

	@Query("SELECT s.id FROM Store s WHERE s.user = :user")
	List<Long> findIdByUser(@Param("user") User user);
  
}
