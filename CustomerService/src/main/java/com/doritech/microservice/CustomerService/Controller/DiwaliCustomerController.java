
package com.doritech.microservice.CustomerService.Controller;

import java.util.List;

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
import org.springframework.web.multipart.MultipartFile;

import com.doritech.microservice.CustomerService.Entity.DiwaliCustomer;
import com.doritech.microservice.CustomerService.Response.ApiResponse;
import com.doritech.microservice.CustomerService.Response.PageResponse;
import com.doritech.microservice.CustomerService.Service.DiwaliCustomerService;

@RestController
@RequestMapping("/api/diwali-customers")
@CrossOrigin(allowedHeaders = "*")
public class DiwaliCustomerController {

	private final DiwaliCustomerService service;

	public DiwaliCustomerController(DiwaliCustomerService service) {
		this.service = service;
	}
	
	@PostMapping("/createCustomer")
	public ResponseEntity<ApiResponse<DiwaliCustomer>> createCustomer(@RequestBody DiwaliCustomer customer) {
		return service.createCustomer(customer);
	}

	@PutMapping("/updateCustomer/{id}")
	public ResponseEntity<ApiResponse<DiwaliCustomer>> updateCustomer(@PathVariable Long id,
			@RequestBody DiwaliCustomer customer) {
		return service.updateCustomer(id, customer);
	}

	@GetMapping("getCustomerById/{id}")
	public ResponseEntity<ApiResponse<DiwaliCustomer>> getCustomerById(@PathVariable Long id) {
		return service.getCustomerById(id);
	}

	@GetMapping("/getAllCustomers")
	public ResponseEntity<ApiResponse<List<DiwaliCustomer>>> getAllCustomers() {
		return service.getAllCustomers();
	}

	@PutMapping("/excel-update")
	public ResponseEntity<ApiResponse<String>> updateCustomersFromExcel(@RequestParam("file") MultipartFile file) {
		return service.updateCustomersFromExcel(file);
	}

	@GetMapping("/getAllCustomerWithPagination")
	public ResponseEntity<ApiResponse<PageResponse<DiwaliCustomer>>> getAllCustomersWithPagination(
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		return service.getAllCustomersWithPagination(page, size);
	}

	@DeleteMapping("/deleteCustomer/{id}")
	public ResponseEntity<ApiResponse<String>> deleteCustomer(@PathVariable Long id) {
		return service.deleteCustomer(id);
	}
}
