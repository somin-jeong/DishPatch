package com.example.dishpatch.infra.db.store.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.dishpatch.infra.db.store.entity.Store;
import com.example.dishpatch.infra.db.user.entity.User;

public interface StoreRepository extends JpaRepository<Store, Long>, StoreQueryRepository {
	int countByUserIdAndDeletedDateIsNull(Long userId);

	Optional<Store> findByIdAndDeletedDateIsNull(Long storeId);

	boolean existsByIdAndDeletedDateIsNull(Long storeId);

	@Query("SELECT s.id FROM Store s WHERE s.user = :user")
	List<Long> findIdByUser(@Param("user") User user);


	@Modifying(clearAutomatically = true,flushAutomatically = true)
	@Query("UPDATE Store s SET s.deletedDate = CURRENT_TIMESTAMP WHERE s.user.id = :userId")
	void deleteByUserId(@Param("userId") Long userId);

}
