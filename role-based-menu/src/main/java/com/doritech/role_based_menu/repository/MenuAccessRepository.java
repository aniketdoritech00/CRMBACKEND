package com.doritech.role_based_menu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doritech.role_based_menu.model.MenuAccessEntity;

@Repository
public interface MenuAccessRepository extends JpaRepository<MenuAccessEntity, Integer> {
	MenuAccessEntity findByRoleId(int userRoleId);

	MenuAccessEntity findByRoleNameAndMenuId(String roleName, int menuId);

	List<MenuAccessEntity> findByRoleName(String empRole);
}
