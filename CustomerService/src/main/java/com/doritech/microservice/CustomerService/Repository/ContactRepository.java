package com.doritech.microservice.CustomerService.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doritech.microservice.CustomerService.Entity.Contact;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    
    List<Contact> findByCustomerId(Long customerId);
    
    Optional<Contact> findByEmail(String email);
    
    List<Contact> findByCustomerIdAndRole(Long customerId, String role);
}