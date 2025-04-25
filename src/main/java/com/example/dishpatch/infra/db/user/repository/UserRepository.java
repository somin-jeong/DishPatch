package com.example.dishpatch.infra.db.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.dishpatch.infra.db.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);

	Optional<User> findByIdAndDeletedDateIsNull(Long userId);
	// Optional<User> findByIdAndDeletedDate(Long userId, LocalDateTime deletedTime);
}
