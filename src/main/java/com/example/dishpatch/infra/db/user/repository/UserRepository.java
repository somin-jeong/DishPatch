package com.example.dishpatch.infra.db.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.dishpatch.infra.db.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
