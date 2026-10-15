package com.doritech.microservice.ApiGateway.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doritech.microservice.ApiGateway.Entity.ApiGatewayLogEntity;

@Repository
public interface ApiGatewayLogRepository extends JpaRepository<ApiGatewayLogEntity, Long> {
}
