package com.example.dishpatch.infra.db.store.repository;

import com.example.dishpatch.infra.db.store.entity.Dib;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DibRepository extends JpaRepository<Dib, Integer> {
}
