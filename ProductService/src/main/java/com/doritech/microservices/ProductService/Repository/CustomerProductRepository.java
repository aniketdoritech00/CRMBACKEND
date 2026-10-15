package com.doritech.microservices.ProductService.Repository;

import com.doritech.microservices.ProductService.Entity.CustomerProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerProductRepository extends JpaRepository<CustomerProduct, Long> {
    
    List<CustomerProduct> findByCustomerId(Long customerId);
    
    List<CustomerProduct> findByProductId(Long productId);
    
    Optional<CustomerProduct> findByCustomerIdAndProductId(Long customerId, Long productId);
    
    boolean existsByCustomerIdAndProductId(Long customerId, Long productId);
    
    Page<CustomerProduct> findByCustomerId(Long customerId, Pageable pageable);
    
    @Query("SELECT cp FROM CustomerProduct cp JOIN FETCH cp.product WHERE cp.customerId = :customerId")
    List<CustomerProduct> findByCustomerIdWithProduct(@Param("customerId") Long customerId);
    
    @Query("SELECT COUNT(cp) FROM CustomerProduct cp WHERE cp.productId = :productId AND cp.status = 'ACTIVE'")
    Long countActiveAssignmentsByProductId(@Param("productId") Long productId);
    
    // NEW METHODS FOR GET ALL CUSTOMER PRODUCTS
    
    /**
     * Find all customer products with product details (for non-paginated results)
     */
    @Query("SELECT cp FROM CustomerProduct cp JOIN FETCH cp.product ORDER BY cp.id DESC")
    List<CustomerProduct> findAllWithProduct();
    
    /**
     * Find all customer products with product details and pagination
     */
    @Query("SELECT cp FROM CustomerProduct cp JOIN FETCH cp.product")
    Page<CustomerProduct> findAllWithProduct(Pageable pageable);
    
    /**
     * Find customer products by status
     */
    List<CustomerProduct> findByStatus(CustomerProduct.ProductStatus status);
    
    /**
     * Find customer products by status with pagination
     */
    Page<CustomerProduct> findByStatus(CustomerProduct.ProductStatus status, Pageable pageable);
    
    /**
     * Find customer products with filters (without pagination)
     */
    @Query("SELECT cp FROM CustomerProduct cp JOIN FETCH cp.product WHERE " +
           "(:status IS NULL OR cp.status = :status) AND " +
           "(:customerId IS NULL OR cp.customerId = :customerId) AND " +
           "(:productId IS NULL OR cp.productId = :productId) " +
           "ORDER BY cp.id DESC")
    List<CustomerProduct> findAllWithFilters(@Param("status") CustomerProduct.ProductStatus status,
                                           @Param("customerId") Long customerId,
                                           @Param("productId") Long productId);
    
    /**
     * Find customer products with filters and pagination
     */
    @Query("SELECT cp FROM CustomerProduct cp JOIN FETCH cp.product WHERE " +
           "(:status IS NULL OR cp.status = :status) AND " +
           "(:customerId IS NULL OR cp.customerId = :customerId) AND " +
           "(:productId IS NULL OR cp.productId = :productId)")
    Page<CustomerProduct> findAllWithFilters(@Param("status") CustomerProduct.ProductStatus status,
                                           @Param("customerId") Long customerId,
                                           @Param("productId") Long productId,
                                           Pageable pageable);
    
    /**
     * Get summary statistics
     */
    @Query("SELECT COUNT(cp) FROM CustomerProduct cp WHERE cp.status = :status")
    Long countByStatus(@Param("status") CustomerProduct.ProductStatus status);
    
    /**
     * Get total count of all assignments
     */
    @Query("SELECT COUNT(cp) FROM CustomerProduct cp")
    Long countAllAssignments();

    @Query(value = "SELECT DISTINCT customer_id FROM customer_products", nativeQuery = true)
    List<Long> findDistinctCustomerIds();
}