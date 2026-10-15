package com.doritech.microservice.CustomerService.Repository;

import com.doritech.microservice.CustomerService.Entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
   boolean existsByName(String name);

   boolean existsByShortCode(String shortCode);
}