package com.example.dishpatch.infra.db.menu.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.dishpatch.infra.db.menu.entity.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long>, MenuQueryRepository {

	@Modifying(clearAutomatically = true)
	@Query("UPDATE Menu m SET m.deletedDate = :now WHERE m.store.id = :storeId AND m.deletedDate IS NULL")
	void bulkSoftDeleteByStoreId(Long storeId, LocalDateTime now);

}
