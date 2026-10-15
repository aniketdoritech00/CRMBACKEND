// ProductService.java
package com.doritech.microservices.ProductService.Service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com .doritech.microservices.ProductService.Request.CustomerProductRequest;
import com.doritech.microservices.ProductService.Request.ProductRequest;
import com.doritech.microservices.ProductService.Request.ProductUpdateRequest;
import com.doritech.microservices.ProductService.Response.CustomerProductResponse;
import com.doritech.microservices.ProductService.Response.ProductResponse;

public interface ProductService {

	 // Product CRUD operations
    ProductResponse createProduct(ProductRequest request);
    
    ProductResponse getProductById(Long id);
    
    ProductResponse getProductByCode(String productCode);
    
    ProductResponse updateProduct(Long id, ProductUpdateRequest request);
    
    void deleteProduct(Long id);
    
    // Product search and listing
    Page<ProductResponse> getAllProducts(Pageable pageable);
    
    Page<ProductResponse> searchProducts(String name, String category, Pageable pageable);
    Page<ProductResponse> searchProducts(String search, Pageable pageable);


    List<String> getAllCategories();
    
    // Customer-Product relationship management
    CustomerProductResponse assignProductToCustomer(CustomerProductRequest request);
    
    void unassignProductFromCustomer(Long customerId, Long productId);
    
    List<CustomerProductResponse> getCustomerProducts(Long customerId);
    
    List<CustomerProductResponse> getProductCustomers(Long productId);
    
    // NEW METHODS - Get All Customer Products
    /**
     * Get all customer-product assignments without pagination
     * @return List of all customer product assignments
     */
    List<CustomerProductResponse> getAllCustomerProducts();
    
    /**
     * Get all customer-product assignments with pagination
     * @param pageable Pagination parameters
     * @return Paginated list of customer product assignments
     */
    Page<CustomerProductResponse> getAllCustomerProducts(Pageable pageable);
    
    /**
     * Get all customer-product assignments with filters (without pagination)
     * @param status Filter by status (ACTIVE, INACTIVE, TRIAL, EXPIRED)
     * @param customerId Filter by specific customer ID (optional)
     * @param productId Filter by specific product ID (optional)
     * @return Filtered list of customer product assignments
     */
    List<CustomerProductResponse> getAllCustomerProducts(String status, Long customerId, Long productId);
    
    /**
     * Get all customer-product assignments with filters and pagination
     * @param status Filter by status (ACTIVE, INACTIVE, TRIAL, EXPIRED)
     * @param customerId Filter by specific customer ID (optional)
     * @param productId Filter by specific product ID (optional)
     * @param pageable Pagination parameters
     * @return Filtered and paginated list of customer product assignments
     */
    Page<CustomerProductResponse> getAllCustomerProducts(String status, Long customerId, Long productId, Pageable pageable);
    
    // Analytics
    Long getActiveAssignmentCount(Long productId);

	List<ProductResponse> getAllProductsNoPagination();
	
	List<CustomerProductResponse> getAllCustomerProductsWithOutPaggination();

	Map<Long, Object> getCustomerWiseAssignmentsWithOutPaggination();

	List<ProductResponse> getAllProductsWithOutPaggination();

	Object getAllCustomerProductsWithPaggination(int page, int size);

	Object getCustomerWiseAssignmentsWithPaggination(int page, int size);

	Object getAllProductsWithPaggination(int page, int size);

	public Object getAllCustomers();



	Object getTop10ProductsByTotalAmount();

	Object getRecentCustomers(int limit);

	Object getProductsByCustomerId(Long customerId);

}