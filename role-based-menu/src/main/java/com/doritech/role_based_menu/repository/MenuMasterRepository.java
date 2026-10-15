package com.doritech.role_based_menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doritech.role_based_menu.model.MenuMasterEntity;

@Repository
public interface MenuMasterRepository extends JpaRepository<MenuMasterEntity, Integer> {
}
