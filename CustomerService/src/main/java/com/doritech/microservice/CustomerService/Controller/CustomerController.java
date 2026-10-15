
package com.doritech.microservice.CustomerService.Controller;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import com.doritech.microservice.CustomerService.Enum.CustomerStatus;
import com.doritech.microservice.CustomerService.Request.CustomerRequest;
import com.doritech.microservice.CustomerService.Response.ApiResponse;
import com.doritech.microservice.CustomerService.Response.ContactResponse;
import com.doritech.microservice.CustomerService.Response.CustomerResponse;
import com.doritech.microservice.CustomerService.Response.PagedResponse;
import com.doritech.microservice.CustomerService.Service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/customers")
@CrossOrigin(allowedHeaders = "*")
@Tag(name = "Customer Management", description = "APIs for managing customers")
public class CustomerController {

	private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

	private final CustomerService customerService;

	CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}

	// 1. Create Customer
	@PostMapping
	@Operation(summary = "Create a new customer", description = "Creates a new customer with addresses and contacts")
	public ResponseEntity<ApiResponse<CustomerResponse>> createCustomer(@Valid @RequestBody CustomerRequest request) {

		log.info("Received request to create customer with gst number: {}", request.getGstNumber());

		CustomerResponse response = customerService.createCustomer(request);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ApiResponse.success("Customer created successfully", response));
	}

	// 2. Get Customer By ID
	@GetMapping("/{id}")
	@Operation(summary = "Get customer by ID", description = "Retrieves customer details by ID")
	public ResponseEntity<ApiResponse<CustomerResponse>> getCustomerById(
			@Parameter(description = "Customer ID") @PathVariable Long id) {

		CustomerResponse response = customerService.getCustomerById(id);

		return ResponseEntity.ok(ApiResponse.success(response));
	}

	// 3. Get Customer By Code
	@GetMapping("/code/{customerCode}")
	@Operation(summary = "Get customer by code", description = "Retrieves customer details by customer code")
	public ResponseEntity<ApiResponse<CustomerResponse>> getCustomerByCode(
			@Parameter(description = "Customer Code") @PathVariable String customerCode) {

		CustomerResponse response = customerService.getCustomerByCode(customerCode);

		return ResponseEntity.ok(ApiResponse.success(response));
	}

	// 4. Update Customer
	@PutMapping("/{id}")
	@Operation(summary = "Update customer", description = "Updates existing customer details")
	public ResponseEntity<ApiResponse<CustomerResponse>> updateCustomer(
			@Parameter(description = "Customer ID") @PathVariable Long id,
			@Valid @RequestBody CustomerRequest request) {

		log.info("Received request to update customer: {}", id);

		CustomerResponse response = customerService.updateCustomer(id, request);

		return ResponseEntity.ok(ApiResponse.success("Customer updated successfully", response));
	}

	// 5. Delete Customer
	@DeleteMapping("/{id}")
	@Operation(summary = "Delete customer", description = "Deletes a customer by ID")
	public ResponseEntity<ApiResponse<Void>> deleteCustomer(
			@Parameter(description = "Customer ID") @PathVariable Long id) {

		log.info("Received request to delete customer: {}", id);

		customerService.deleteCustomer(id);

		return ResponseEntity.ok(ApiResponse.success("Customer deleted successfully", null));
	}

	// 6. Get All Customers With Pagination
	@GetMapping
	@Operation(summary = "Get all customers", description = "Retrieves all customers with pagination")
	public ResponseEntity<ApiResponse<PagedResponse<CustomerResponse>>> getAllCustomers(
			@Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,

			@Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,

			@Parameter(description = "Sort field") @RequestParam(defaultValue = "id") String sortBy,

			@Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDirection) {

		Sort sort = Sort.by(Direction.fromString(sortDirection), sortBy);

		Pageable pageable = PageRequest.of(page, size, sort);

		PagedResponse<CustomerResponse> response = customerService.getAllCustomers(pageable);

		return ResponseEntity.ok(ApiResponse.success(response));
	}

	// 7. Search Customers
	@GetMapping("/search")
	@Operation(summary = "Search customers", description = "Search customers with filters")
	public ResponseEntity<ApiResponse<PagedResponse<CustomerResponse>>> searchCustomers(
			@Parameter(description = "Customer name filter") @RequestParam(required = false) String name,

			@Parameter(description = "Industry filter") @RequestParam(required = false) String industry,

			@Parameter(description = "Status filter") @RequestParam(required = false) CustomerStatus status,

			@Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,

			@Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,

			@Parameter(description = "Sort field") @RequestParam(defaultValue = "id") String sortBy,

			@Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDirection) {

		Sort sort = Sort.by(Direction.fromString(sortDirection), sortBy);

		Pageable pageable = PageRequest.of(page, size, sort);

		PagedResponse<CustomerResponse> response = customerService.searchCustomers(name, industry, status, pageable);

		return ResponseEntity.ok(ApiResponse.success(response));
	}

	// 8. Get Customers By Status
	@GetMapping("/by-status/{status}")
	@Operation(summary = "Get customers by status", description = "Retrieves customers filtered by status")
	public ResponseEntity<ApiResponse<List<CustomerResponse>>> getCustomersByStatus(
			@Parameter(description = "Customer status") @PathVariable CustomerStatus status) {

		List<CustomerResponse> response = customerService.getCustomersByStatus(status);

		return ResponseEntity.ok(ApiResponse.success(response));
	}

	// 9. Get Customers By Tag
	@GetMapping("/by-tag/{tagId}")
	@Operation(summary = "Get customers by tag", description = "Retrieves customers associated with a specific tag")
	public ResponseEntity<ApiResponse<List<CustomerResponse>>> getCustomersByTag(
			@Parameter(description = "Tag ID") @PathVariable Long tagId) {

		List<CustomerResponse> response = customerService.getCustomersByTag(tagId);

		return ResponseEntity.ok(ApiResponse.success(response));
	}

	// 10. Add Tag To Customer
	@PostMapping("/{customerId}/tags/{tagId}")
	@Operation(summary = "Add tag to customer", description = "Associates a tag with a customer")
	public ResponseEntity<ApiResponse<CustomerResponse>> addTagToCustomer(
			@Parameter(description = "Customer ID") @PathVariable Long customerId,

			@Parameter(description = "Tag ID") @PathVariable Long tagId) {

		CustomerResponse response = customerService.addTagToCustomer(customerId, tagId);

		return ResponseEntity.ok(ApiResponse.success("Tag added to customer successfully", response));
	}

	// 11. Remove Tag From Customer
	@DeleteMapping("/{customerId}/tags/{tagId}")
	@Operation(summary = "Remove tag from customer", description = "Removes tag association from customer")
	public ResponseEntity<ApiResponse<Void>> removeTagFromCustomer(
			@Parameter(description = "Customer ID") @PathVariable Long customerId,

			@Parameter(description = "Tag ID") @PathVariable Long tagId) {

		customerService.removeTagFromCustomer(customerId, tagId);

		return ResponseEntity.ok(ApiResponse.success("Tag removed from customer successfully", null));
	}

	// 12. Get Customer Count By Status
	@GetMapping("/count/by-status/{status}")
	@Operation(summary = "Get customer count by status", description = "Returns count of customers by status")
	public ResponseEntity<ApiResponse<Long>> getCustomerCountByStatus(
			@Parameter(description = "Customer status") @PathVariable CustomerStatus status) {

		long count = customerService.getCustomerCountByStatus(status);

		return ResponseEntity.ok(ApiResponse.success("Customer count retrieved successfully", count));
	}

	// 13. Get All Customers Without Pagination
	@GetMapping("/all")
	@Operation(summary = "Get all customers (no pagination)", description = "Retrieves all customers without pagination")
	public ResponseEntity<ApiResponse<List<CustomerResponse>>> getAllCustomersNoPagination() {

		List<CustomerResponse> customers = customerService.getAllCustomers();

		ApiResponse<List<CustomerResponse>> response = ApiResponse.success("Customers retrieved successfully",
				customers);

		return ResponseEntity.ok(response);
	}

	// 14. Get Customer ID And Name List
	@GetMapping("/id-name-list")
	public List<Map<String, Object>> getCustomerIdAndNameList() {

		try {
			return customerService.getCustomerIdAndNameList();
		} catch (Exception e) {
			log.error("Error fetching customer ID and name list", e);
			return Collections.emptyList();
		}
	}

	// 15. Get Contact By Email
	@GetMapping("/by-email")
	public ApiResponse<ContactResponse> getContactByEmail(@RequestParam String email) {

		try {
			ContactResponse response = customerService.getContactByEmail(email);

			return ApiResponse.success("Contact fetched successfully", response);

		} catch (RuntimeException ex) {
			return ApiResponse.error(ex.getMessage());

		} catch (Exception ex) {
			log.error("Error fetching contact by email", ex);

			return ApiResponse.error("Unexpected error: " + ex.getMessage());
		}
	}

	// 16. Get Customers By Organization
	@GetMapping("/organization")
	public ApiResponse<List<Map<String, Object>>> getCustomersByOrganization(@RequestParam String organization) {

		return customerService.getCustomersByOrganization(organization);
	}

	// 17. Get Customers By Filter
	@GetMapping("/getCustomersByFilter")
	public ResponseEntity<Object> getCustomersByFilter(@RequestParam(required = false) String officeBranch,
			@RequestParam(required = false) String bankName, @RequestParam(required = false) String zoneName,
			@RequestParam(required = false) String ifsc, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Pageable pageable = PageRequest.of(page, size);

		return ResponseEntity
				.ok(customerService.getCustomersByFilter(officeBranch, bankName, zoneName, ifsc, pageable));
	}

	// 18. Get Office Branches
	@GetMapping("/office-branches")
	public ResponseEntity<Object> getOfficeBranches() {

		return ResponseEntity.ok(customerService.getOfficeBranches());
	}

	// 19. Get Bank Names By Office Branch
	@GetMapping("/bank-names")
	public ResponseEntity<Object> getBankNames(@RequestParam String officeBranch) {

		return ResponseEntity.ok(customerService.getBankNamesByOfficeBranch(officeBranch));
	}

	// 20. Get Zones
	@GetMapping("/zones")
	public ResponseEntity<Object> getZones(@RequestParam String officeBranch, @RequestParam String bankName) {

		return ResponseEntity.ok(customerService.getZoneNames(officeBranch, bankName));
	}

	// 21. Get Emails
	@GetMapping("/emails")
	public ResponseEntity<Object> getEmails(@RequestParam(required = false) String officeBranch,
			@RequestParam(required = false) String bankName, @RequestParam(required = false) String zoneName) {

		return ResponseEntity.ok(customerService.getEmails(officeBranch, bankName, zoneName));
	}

	// 22. Get Details By IFSC
	@GetMapping("/getDetailsByIfsc")
	public ResponseEntity<Object> getDetailsByIfsc(@RequestParam String ifsc) {

		return ResponseEntity.ok(customerService.getDetailsByIfsc(ifsc));
	}

	// 23. Get Details By Email
	@GetMapping("/getDetailsByEmail")
	public ResponseEntity<Object> getDetailsByEmail(@RequestParam String email) {

		return ResponseEntity.ok(customerService.getDetailsByEmail(email));
	}

	@GetMapping("/template/download")
	public ResponseEntity<Resource> downloadTemplate() throws IOException {
		Resource resource = new ClassPathResource("templates/customer_template.xlsx");

		return ResponseEntity.ok()
				.contentType(
						MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"customer_template.xlsx\"")
				.body(resource);
	}
}
