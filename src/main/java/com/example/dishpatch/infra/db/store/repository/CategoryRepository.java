package com.example.dishpatch.infra.db.store.repository;

import com.example.dishpatch.infra.db.store.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
