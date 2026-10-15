
package com.doritech.microservice.CustomerService.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.doritech.microservice.CustomerService.Entity.DiwaliCustomer;

public interface DiwaliCustomerRepository extends JpaRepository<DiwaliCustomer, Long> {
}
