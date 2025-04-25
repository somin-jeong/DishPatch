package com.example.dishpatch.infra.db.menu.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.dishpatch.infra.db.menu.entity.MenuOption;

import io.lettuce.core.dynamic.annotation.Param;

public interface MenuOptionRepository extends JpaRepository<MenuOption, Long>, MenuOptionQueryRepository {
	@Modifying(clearAutomatically = true)
	@Query("""
			UPDATE MenuOption mo
			SET mo.deletedDate = :now
			WHERE mo.menu.id IN (
				SELECT m.id FROM Menu m WHERE m.store.id = :storeId AND m.deletedDate IS NOT NULL
			)
		""")
	int bulkSoftDeleteByStoreId(Long storeId, LocalDateTime now);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query(value = """
     UPDATE MenuOption mo
     JOIN Menu m ON mo.menu_id = m.id
     JOIN Store s ON m.store_id = s.id
     SET mo.deleted_date = current_timestamp
     WHERE s.user_id = :userId
""", nativeQuery = true)
	void deleteByUserId(@Param("userId") Long userId);

}
