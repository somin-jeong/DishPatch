package com.example.dishpatch.infra.db.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dishpatch.infra.db.store.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
