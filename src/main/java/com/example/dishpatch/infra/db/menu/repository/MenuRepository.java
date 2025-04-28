package com.example.dishpatch.infra.db.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.dishpatch.infra.db.menu.entity.Menu;

import io.lettuce.core.dynamic.annotation.Param;

public interface MenuRepository extends JpaRepository<Menu, Long>, MenuQueryRepository {

	@Modifying(clearAutomatically = true)
	@Query("UPDATE Menu m SET m.deletedDate = CURRENT_TIMESTAMP WHERE m.store.id = :storeId")
	void bulkSoftDeleteByStoreId(Long storeId);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query(value = """
				UPDATE menu m
		      	JOIN store s ON m.store_id = s.id
		        SET m.deleted_date = CURRENT_TIMESTAMP
		        WHERE s.user_id = :userId
		""", nativeQuery = true)
	void deleteByUserId(@Param("userId") Long userId);
}
