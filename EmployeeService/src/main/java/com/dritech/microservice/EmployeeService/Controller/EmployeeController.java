package com.dritech.microservice.EmployeeService.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

import com.dritech.microservice.EmployeeService.Enum.Department;
import com.dritech.microservice.EmployeeService.Enum.EmployeeStatus;
import com.dritech.microservice.EmployeeService.Request.EmployeeRequest;
import com.dritech.microservice.EmployeeService.Response.EmployeeResponse;
import com.dritech.microservice.EmployeeService.Service.EmployeeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/employee")
@CrossOrigin(allowedHeaders = "*")
public class EmployeeController {

	private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);

	final EmployeeService employeeService;

	EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@PostMapping
	public ResponseEntity<EmployeeResponse> createEmployee(@Valid @RequestBody EmployeeRequest request) {
		log.info("Creating employee: {}", request.getEmployeeId());
		EmployeeResponse response = employeeService.createEmployee(request);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable Long id) {
		log.info("Fetching employee by ID: {}", id);
		EmployeeResponse response = employeeService.getEmployeeById(id);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/employee-id/{employeeId}")
	public ResponseEntity<EmployeeResponse> getEmployeeByEmployeeId(@PathVariable String employeeId) {
		log.info("Fetching employee by Employee ID: {}", employeeId);
		EmployeeResponse response = employeeService.getEmployeeByEmployeeId(employeeId);
		return ResponseEntity.ok(response);
	}

	@GetMapping
	public ResponseEntity<Page<EmployeeResponse>> getAllEmployees(
			@PageableDefault(size = 10, sort = "id") Pageable pageable) {
		log.info("Fetching all employees with pagination");
		Page<EmployeeResponse> responses = employeeService.getAllEmployees(pageable);
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/all")
	public ResponseEntity<List<EmployeeResponse>> getAllEmployeesWithoutPagination() {
		log.info("Fetching all employees without pagination");
		List<EmployeeResponse> responses = employeeService.getAllEmployees();
		return ResponseEntity.ok(responses);
	}

	@PutMapping("/{id}")
	public ResponseEntity<EmployeeResponse> updateEmployee(@PathVariable Long id,
			@Valid @RequestBody EmployeeRequest request) {
		log.info("Updating employee with ID: {}", id);
		EmployeeResponse response = employeeService.updateEmployee(id, request);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
		log.info("Deleting employee with ID: {}", id);
		employeeService.deleteEmployee(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/department/{department}")
	public ResponseEntity<List<EmployeeResponse>> getEmployeesByDepartment(@PathVariable Department department) {
		log.info("Fetching employees by department: {}", department);
		List<EmployeeResponse> responses = employeeService.getEmployeesByDepartment(department);
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/status/{status}")
	public ResponseEntity<List<EmployeeResponse>> getEmployeesByStatus(@PathVariable EmployeeStatus status) {
		log.info("Fetching employees by status: {}", status);
		List<EmployeeResponse> responses = employeeService.getEmployeesByStatus(status);
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/manager/{managerId}")
	public ResponseEntity<List<EmployeeResponse>> getEmployeesByManager(@PathVariable String managerId) {
		log.info("Fetching employees by manager: {}", managerId);
		List<EmployeeResponse> responses = employeeService.getEmployeesByManager(managerId);
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/search")
	public ResponseEntity<Page<EmployeeResponse>> searchEmployees(@RequestParam String keyword,
			@PageableDefault(size = 10, sort = "id") Pageable pageable) {
		log.info("Searching employees with keyword: {}", keyword);
		Page<EmployeeResponse> responses = employeeService.searchEmployees(keyword, pageable);
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/exists/employee-id/{employeeId}")
	public ResponseEntity<Boolean> checkEmployeeIdExists(@PathVariable String employeeId) {
		boolean exists = employeeService.existsByEmployeeId(employeeId);
		return ResponseEntity.ok(exists);
	}

	@GetMapping("/exists/email/{email}")
	public ResponseEntity<Boolean> checkEmailExists(@PathVariable String email) {
		boolean exists = employeeService.existsByEmail(email);
		return ResponseEntity.ok(exists);
	}

	@GetMapping("/count/department/{department}")
	public ResponseEntity<Long> countByDepartment(@PathVariable Department department) {
		long count = employeeService.countByDepartment(department);
		return ResponseEntity.ok(count);
	}

	@GetMapping("/count/status/{status}")
	public ResponseEntity<Long> countByStatus(@PathVariable EmployeeStatus status) {
		long count = employeeService.countByStatus(status);
		return ResponseEntity.ok(count);
	}

	@GetMapping("/search-by-department")
	public ResponseEntity<Page<EmployeeResponse>> searchEmployeesByDepartment(@RequestParam Department department,
			@PageableDefault(size = 10, sort = "id") Pageable pageable) {
		log.info("Searching employees by department: {}", department);
		Page<EmployeeResponse> responses = employeeService.searchEmployeesByDepartment(department, pageable);
		return ResponseEntity.ok(responses);
	}

}