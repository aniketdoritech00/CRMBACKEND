package com.dritech.microservice.EmployeeService.Service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dritech.microservice.EmployeeService.Enum.Department;
import com.dritech.microservice.EmployeeService.Enum.EmployeeStatus;
import com.dritech.microservice.EmployeeService.Request.EmployeeRequest;
import com.dritech.microservice.EmployeeService.Response.EmployeeResponse;

public interface EmployeeService {

	EmployeeResponse createEmployee(EmployeeRequest request);

	EmployeeResponse getEmployeeById(Long id);

	EmployeeResponse getEmployeeByEmployeeId(String employeeId);

	List<EmployeeResponse> getAllEmployees();

	Page<EmployeeResponse> getAllEmployees(Pageable pageable);

	EmployeeResponse updateEmployee(Long id, EmployeeRequest request);

	void deleteEmployee(Long id);

	List<EmployeeResponse> getEmployeesByDepartment(Department department);

	List<EmployeeResponse> getEmployeesByStatus(EmployeeStatus status);

	List<EmployeeResponse> getEmployeesByManager(String managerId);

	Page<EmployeeResponse> searchEmployees(String keyword, Pageable pageable);

	boolean existsByEmployeeId(String employeeId);

	boolean existsByEmail(String email);

	long countByDepartment(Department department);

	long countByStatus(EmployeeStatus status);

	Page<EmployeeResponse> searchEmployeesByDepartment(Department department, Pageable pageable);
}
