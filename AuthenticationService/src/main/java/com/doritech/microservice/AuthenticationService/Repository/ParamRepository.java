package com.doritech.microservice.AuthenticationService.Repository;

import com.doritech.microservice.AuthenticationService.Entity.ParamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParamRepository extends JpaRepository<ParamEntity, Long> {
   ParamEntity findByCode(String string);
}