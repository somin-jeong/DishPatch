package com.example.dishpatch.infra.db.cart.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.dishpatch.infra.db.cart.entity.Cart;

import io.lettuce.core.dynamic.annotation.Param;

public interface CartRepository extends JpaRepository<Cart, Long> {
	@Modifying(clearAutomatically = true)
	void deleteAllByStoreId(Long storeId);

	List<Cart> findByUserId(Long userId);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("UPDATE Cart c SET c.deletedDate = current_timestamp WHERE c.user.id = :userId")
		//@Query("DELETE FROM Cart c WHERE c.user.id = :userId")
	void deleteAllByUserId(@Param("userId") Long userId);

	@Modifying(clearAutomatically = true)
	void deleteAllById(Long cartId);
	
	@Modifying
	@Query("DELETE FROM Cart c WHERE c.modifiedDate <= :expiredTime")
	void deleteAllExpired(@Param("expiredTime") LocalDateTime expiredTime);

}
