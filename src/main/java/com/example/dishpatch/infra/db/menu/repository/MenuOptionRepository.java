package com.example.dishpatch.infra.db.menu.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.dishpatch.infra.db.menu.entity.MenuOption;

public interface MenuOptionRepository extends JpaRepository<MenuOption, Long> {
	@Modifying(clearAutomatically = true)
	@Query("""
			UPDATE MenuOption mo
			SET mo.deletedDate = :now
			WHERE mo.menu.id IN (
				SELECT m.id FROM Menu m WHERE m.store.id = :storeId AND m.deletedDate IS NOT NULL
			)
		""")
	int bulkSoftDeleteByStoreId(Long storeId, LocalDateTime now);

}
