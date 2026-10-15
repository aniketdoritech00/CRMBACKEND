package com.doritech.role_based_menu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.doritech.role_based_menu.model.ParamEntity;

@Repository
@EnableJpaRepositories
public interface ParamRepository extends JpaRepository<ParamEntity, Long> {

	ParamEntity findByCode(String code);

	ParamEntity getParamByCodeAndSerial(String code, String serial);

	List<ParamEntity> findBySerial(String serial);

	ParamEntity findByserial(String serial);

	@Query(value = "SELECT descp1 FROM param_entity WHERE code = :code", nativeQuery = true)
	String findDescp1ByCode(@Param("code") String code);

}
