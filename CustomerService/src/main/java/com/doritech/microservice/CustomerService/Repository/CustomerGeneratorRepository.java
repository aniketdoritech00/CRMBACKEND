package com.doritech.microservice.CustomerService.Repository;

import com.doritech.microservice.CustomerService.Entity.CustomerGenerator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.util.Optional;
@Repository
public interface CustomerGeneratorRepository extends JpaRepository<CustomerGenerator, Integer> {
}