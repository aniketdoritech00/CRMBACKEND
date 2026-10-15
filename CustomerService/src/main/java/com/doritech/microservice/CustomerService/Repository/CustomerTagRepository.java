package com.doritech.microservice.CustomerService.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.doritech.microservice.CustomerService.Entity.CustomerTag;

import jakarta.transaction.Transactional;

@Repository
public interface CustomerTagRepository extends JpaRepository<CustomerTag, Long> {

	List<CustomerTag> findByCustomerId(Long customerId);

	List<CustomerTag> findByTagId(Long tagId);

	Optional<CustomerTag> findByCustomerIdAndTagId(Long customerId, Long tagId);

	void deleteByCustomerIdAndTagId(Long customerId, Long tagId);
	@Modifying
    @Transactional
    @Query("DELETE FROM CustomerTag ct WHERE ct.customer.id = :customerId")
    void deleteByCustomerId(Long customerId);
	@Query("SELECT ct FROM CustomerTag ct JOIN FETCH ct.tag WHERE ct.customer.id = :customerId")
	List<CustomerTag> findByCustomerIdWithTag(@Param("customerId") Long customerId);
}