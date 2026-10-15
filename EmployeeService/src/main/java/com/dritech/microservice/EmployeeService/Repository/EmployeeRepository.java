package com.dritech.microservice.EmployeeService.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dritech.microservice.EmployeeService.Entity.Employee;
import com.dritech.microservice.EmployeeService.Enum.Department;
import com.dritech.microservice.EmployeeService.Enum.EmployeeStatus;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	Optional<Employee> findByEmployeeId(String employeeId);

	Optional<Employee> findByEmail(String email);

	List<Employee> findByDepartment(Department department);

	List<Employee> findByStatus(EmployeeStatus status);

	List<Employee> findByManagerId(String managerId);

	@Query("SELECT e FROM Employee e WHERE " + "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
			+ "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
			+ "LOWER(e.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
			+ "LOWER(e.employeeId) LIKE LOWER(CONCAT('%', :keyword, '%'))")
	Page<Employee> searchEmployees(@Param("keyword") String keyword, Pageable pageable);

	boolean existsByEmployeeId(String employeeId);

	boolean existsByEmail(String email);

	long countByDepartment(Department department);

	long countByStatus(EmployeeStatus status);
	
	Page<Employee> findByDepartment(Department department, Pageable pageable);
	
}
