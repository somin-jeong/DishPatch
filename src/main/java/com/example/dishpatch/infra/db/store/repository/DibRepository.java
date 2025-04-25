package com.example.dishpatch.infra.db.store.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.dishpatch.infra.db.store.entity.Dib;

import io.lettuce.core.dynamic.annotation.Param;

public interface DibRepository extends JpaRepository<Dib, Long> {
	boolean existsByUserIdAndStoreId(Long userId, Long storeId);

	Optional<Dib> findByUserIdAndStoreId(Long id, Long storeId);

	@Modifying(clearAutomatically = true)
	void deleteAllByStoreId(Long storeId);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query(value = """
    	DELETE
    	FROM dib d
    	JOIN store s ON d.store_id = s.id
    	WHERE s.user_id = :userId
""",nativeQuery = true)
	void deleteByUserId(@Param("userId") Long userId);
}
