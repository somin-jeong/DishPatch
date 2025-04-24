package com.example.dishpatch.infra.db.menu.repository;

import java.util.Optional;

import com.example.dishpatch.infra.db.menu.entity.MenuOption;

public interface MenuOptionQueryRepository {

	Optional<MenuOption> findByIdWithMenuAndStore(Long id);
}
