package com.example.dishpatch.infra.db.menu.repository;

import com.example.dishpatch.infra.db.menu.entity.MenuOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuOptionRepository extends JpaRepository<MenuOption, Long> {

}
