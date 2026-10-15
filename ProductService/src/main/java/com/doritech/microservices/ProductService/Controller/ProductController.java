package com.doritech.microservices.ProductService.Controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doritech.microservices.ProductService.Request.CustomerProductRequest;
import com.doritech.microservices.ProductService.Request.ProductRequest;
import com.doritech.microservices.ProductService.Request.ProductUpdateRequest;
import com.doritech.microservices.ProductService.Response.ApiResponse;
import com.doritech.microservices.ProductService.Response.CustomerProductResponse;
import com.doritech.microservices.ProductService.Response.ProductResponse;
import com.doritech.microservices.ProductService.Service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/product/v1")
@CrossOrigin(allowedHeaders = "*")
public class ProductController {

	private static final Logger log = LoggerFactory.getLogger(ProductController.class);
	
	@Autowired
	ProductService productService;

	@PostMapping("/products")
	public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
		log.info("Request to create product: {}", request.getProductCode());
		ProductResponse response = productService.createProduct(request);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping("/products/searchAll")
	public ResponseEntity<Page<ProductResponse>> searchProducts(@RequestParam(required = false) String search,
			Pageable pageable) {

		log.info("Request to search all products by: {}", search);
		Page<ProductResponse> response = productService.searchProducts(search, pageable);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/products/{id}")
	public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
		log.info("Request to get product with ID: {}", id);
		ProductResponse response = productService.getProductById(id);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/products/code/{productCode}")
	public ResponseEntity<ProductResponse> getProductByCode(@PathVariable String productCode) {
		log.info("Request to get product with code: {}", productCode);
		ProductResponse response = productService.getProductByCode(productCode);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/products/{id}")
	public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id,
			@Valid @RequestBody ProductUpdateRequest request) {
		log.info("Request to update product with ID: {}", id);
		ProductResponse response = productService.updateProduct(id, request);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/products/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
		log.info("Request to delete product with ID: {}", id);
		productService.deleteProduct(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/products")
	public ResponseEntity<Page<ProductResponse>> getAllProducts(Pageable pageable) {
		log.info("Request to get all products with pagination: page={}, size={}", pageable.getPageNumber(),
				pageable.getPageSize());
		Page<ProductResponse> response = productService.getAllProducts(pageable);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/products/search")
	public ResponseEntity<Page<ProductResponse>> searchProducts(@RequestParam(required = false) String name,
			@RequestParam(required = false) String category, Pageable pageable) {
		log.info("Request to search products - name: {}, category: {}", name, category);
		Page<ProductResponse> response = productService.searchProducts(name, category, pageable);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/products/categories")
	public ResponseEntity<List<String>> getAllCategories() {
		log.info("Request to get all product categories");
		List<String> categories = productService.getAllCategories();
		return ResponseEntity.ok(categories);
	}

	@PostMapping("/customers/{customerId}/products/{productId}")
	public ResponseEntity<CustomerProductResponse> assignProductToCustomer(@PathVariable Long customerId,
			@PathVariable Long productId, @Valid @RequestBody CustomerProductRequest request) {
		log.info("Request to assign product {} to customer {}", productId, customerId);
		request.setCustomerId(customerId);
		request.setProductId(productId);
		CustomerProductResponse response = productService.assignProductToCustomer(request);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@DeleteMapping("/customers/{customerId}/products/{productId}")
	public ResponseEntity<Void> unassignProductFromCustomer(@PathVariable Long customerId,
			@PathVariable Long productId) {
		log.info("Request to unassign product {} from customer {}", productId, customerId);
		productService.unassignProductFromCustomer(customerId, productId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/customers/{customerId}/products")
	public ResponseEntity<List<CustomerProductResponse>> getCustomerProducts(@PathVariable Long customerId) {
		log.info("Request to get products for customer: {}", customerId);
		List<CustomerProductResponse> response = productService.getCustomerProducts(customerId);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/products/{productId}/customers")
	public ResponseEntity<List<CustomerProductResponse>> getProductCustomers(@PathVariable Long productId) {
		log.info("Request to get customers for product: {}", productId);
		List<CustomerProductResponse> response = productService.getProductCustomers(productId);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/products/{productId}/assignments/count")
	public ResponseEntity<Long> getActiveAssignmentCount(@PathVariable Long productId) {
		log.info("Request to get active assignment count for product: {}", productId);
		Long count = productService.getActiveAssignmentCount(productId);
		return ResponseEntity.ok(count);
	}

	@GetMapping("/all")
	public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProductsNoPagination() {
		log.info("Request to get all products without pagination");

		List<ProductResponse> response = productService.getAllProductsNoPagination();
		return ResponseEntity.ok(ApiResponse.success("All products retrieved successfully", response));
	}

	@GetMapping("/customer-products/getAllCustomerProductsWithOutPaggination")
	public ResponseEntity<List<CustomerProductResponse>> getAllCustomerProductsWithOutPaggination() {
		log.info("Request to get all customer-product assignments without pagination");
		List<CustomerProductResponse> response = productService.getAllCustomerProductsWithOutPaggination();
		return ResponseEntity.ok(response);
	}

	@GetMapping("/customer-products/getCustomerWiseAssignmentsWithOutPaggination")
	public ResponseEntity<Map<Long, Object>> getCustomerWiseAssignmentsWithOutPaggination() {
		log.info("Request to get customer-wise grouped assignments");
		Map<Long, Object> response = productService.getCustomerWiseAssignmentsWithOutPaggination();
		return ResponseEntity.ok(response);
	}

	@GetMapping("/products/getAllProductsWithOutPaggination")
	public ResponseEntity<List<ProductResponse>> getAllProductsWithOutPaggination() {
		log.info("Request to get product assignments");
		List<ProductResponse> response = productService.getAllProductsWithOutPaggination();
		return ResponseEntity.ok(response);
	}

	@GetMapping("/customer-products/getAllCustomerProductsWithPaggination")
	public ResponseEntity<Object> getAllCustomerProductsWithPaggination(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		log.info("Request to get all customer-product assignments with pagination: page {}, size {}", page, size);
		return ResponseEntity.ok(productService.getAllCustomerProductsWithPaggination(page, size));
	}

	@GetMapping("/customer-products/getCustomerWiseAssignmentsWithPaggination")
	public ResponseEntity<Object> getCustomerWiseAssignmentsWithPaggination(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		log.info("Request to get customer-wise grouped assignments with pagination: page {}, size {}", page, size);
		return ResponseEntity.ok(productService.getCustomerWiseAssignmentsWithPaggination(page, size));
	}

	@GetMapping("/products/getAllProductsWithPaggination")
	public ResponseEntity<Object> getAllProductsWithPaggination(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		log.info("Request to get all products with pagination: page {}, size {}", page, size);
		return ResponseEntity.ok(productService.getAllProductsWithPaggination(page, size));
	}

	@GetMapping("/getAllCustomers")
	public ResponseEntity<Object> getAllCustomers() {
	    Object response = productService.getAllCustomers();
	    return ResponseEntity.ok(response);
	}

	@GetMapping("/getTop10Products")
	public ResponseEntity<Object> getTop10Products() {
		return ResponseEntity.ok(productService.getTop10ProductsByTotalAmount());
	}

	@GetMapping("/getRecentCustomers")
	public ResponseEntity<Object> getRecentCustomers(@RequestParam(defaultValue = "5") int limit) {
		return ResponseEntity.ok(productService.getRecentCustomers(limit));
	}
	@GetMapping("/getProductsByCustomer/{customerId}")
	public ResponseEntity<Object> getProductsByCustomerId(@PathVariable Long customerId) {
		return ResponseEntity.ok(productService.getProductsByCustomerId(customerId));
	}
}
