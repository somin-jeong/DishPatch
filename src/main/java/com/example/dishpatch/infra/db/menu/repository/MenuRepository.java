package com.example.dishpatch.infra.db.menu.repository;

import com.example.dishpatch.infra.db.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

}
