package com.example.dishpatch.infra.db.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dishpatch.infra.db.store.entity.Dib;

public interface DibRepository extends JpaRepository<Dib, Integer> {
}
