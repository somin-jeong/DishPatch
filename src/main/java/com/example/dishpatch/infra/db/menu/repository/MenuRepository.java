package com.example.dishpatch.infra.db.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dishpatch.infra.db.menu.entity.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long>, MenuQueryRepository {

}
