
package com.doritech.microservice.CustomerService.Service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.doritech.microservice.CustomerService.Entity.DiwaliCustomer;
import com.doritech.microservice.CustomerService.Response.ApiResponse;
import com.doritech.microservice.CustomerService.Response.PageResponse;

public interface DiwaliCustomerService {

	ResponseEntity<ApiResponse<DiwaliCustomer>> createCustomer(DiwaliCustomer customer);

	ResponseEntity<ApiResponse<DiwaliCustomer>> updateCustomer(Long id, DiwaliCustomer customer);

	ResponseEntity<ApiResponse<DiwaliCustomer>> getCustomerById(Long id);

	ResponseEntity<ApiResponse<List<DiwaliCustomer>>> getAllCustomers();

	ResponseEntity<ApiResponse<String>> updateCustomersFromExcel(MultipartFile file);

	ResponseEntity<ApiResponse<PageResponse<DiwaliCustomer>>> getAllCustomersWithPagination(int page, int size);

	ResponseEntity<ApiResponse<String>> deleteCustomer(Long id);
}
