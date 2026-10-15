package com.dritech.microservice.EmployeeService.ServiceImpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.doritech.microservice.CustomerService.Exception.GlobalException;
import com.dritech.microservice.EmployeeService.Entity.Employee;
import com.dritech.microservice.EmployeeService.Enum.Department;
import com.dritech.microservice.EmployeeService.Enum.EmployeeStatus;
import com.dritech.microservice.EmployeeService.Mapper.EmployeeMapper;
import com.dritech.microservice.EmployeeService.Repository.EmployeeRepository;
import com.dritech.microservice.EmployeeService.Request.EmployeeRequest;
import com.dritech.microservice.EmployeeService.Response.EmployeeResponse;
import com.dritech.microservice.EmployeeService.Service.EmployeeService;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

	private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

	final EmployeeRepository employeeRepository;

	final EmployeeMapper employeeMapper;

	final PasswordEncoder passwordEncoder;

	EmployeeServiceImpl(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder,
			EmployeeMapper employeeMapper) {
		this.employeeRepository = employeeRepository;
		this.passwordEncoder = passwordEncoder;
		this.employeeMapper = employeeMapper;
	}

	@Override
	public EmployeeResponse createEmployee(EmployeeRequest request) {
		if (employeeRepository.existsByEmail(request.getEmail())) {
			throw new GlobalException("Employee with this email already exists");
		}
		Employee employee = employeeMapper.toEntity(request);
		employee.setPassword(passwordEncoder.encode(request.getPassword()));
		Employee savedEmployee = employeeRepository.save(employee);
		log.info("Employee created successfully with ID: {}", savedEmployee.getId());
		return employeeMapper.toResponse(savedEmployee);
	}

	@Override
	@Transactional(readOnly = true)
	public EmployeeResponse getEmployeeById(Long id) {
		log.info("Fetching employee with ID: {}", id);

		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new GlobalException("Employee not found with ID: " + id));

		return employeeMapper.toResponse(employee);
	}

	@Override
	@Transactional(readOnly = true)
	public EmployeeResponse getEmployeeByEmployeeId(String employeeId) {
		log.info("Fetching employee with Employee ID: {}", employeeId);
		Employee employee = employeeRepository.findByEmployeeId(employeeId)
				.orElseThrow(() -> new GlobalException("Employee not found with Employee ID: " + employeeId));
		return employeeMapper.toResponse(employee);
	}

	@Override
	@Transactional(readOnly = true)
	public List<EmployeeResponse> getAllEmployees() {
		log.info("Fetching all employees");
		List<Employee> employees = employeeRepository.findAll();
		return employeeMapper.toResponseList(employees);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<EmployeeResponse> getAllEmployees(Pageable pageable) {
		log.info("Fetching employees with pagination: {}", pageable);
		Page<Employee> employees = employeeRepository.findAll(pageable);
		return employees.map(employeeMapper::toResponse);
	}

	@Override
	public EmployeeResponse updateEmployee(Long id, EmployeeRequest request) {
		log.info("Updating employee with ID: {}", id);

		Employee existingEmployee = employeeRepository.findById(id)
				.orElseThrow(() -> new GlobalException("Employee not found with ID: " + id));

		if (!existingEmployee.getEmployeeId().equals(request.getEmployeeId())
				&& employeeRepository.existsByEmployeeId(request.getEmployeeId())) {
			throw new GlobalException("Employee with ID " + request.getEmployeeId() + " already exists");
		}

		if (!existingEmployee.getEmail().equals(request.getEmail())
				&& employeeRepository.existsByEmail(request.getEmail())) {
			throw new GlobalException("Employee with email " + request.getEmail() + " already exists");
		}

		employeeMapper.updateEntityFromRequest(request, existingEmployee);
		Employee updatedEmployee = employeeRepository.save(existingEmployee);

		log.info("Employee updated successfully with ID: {}", updatedEmployee.getId());
		return employeeMapper.toResponse(updatedEmployee);
	}

	@Override
	public void deleteEmployee(Long id) {
		log.info("Deleting employee with ID: {}", id);

		if (!employeeRepository.existsById(id)) {
			throw new GlobalException("Employee not found with ID: " + id);
		}

		employeeRepository.deleteById(id);
		log.info("Employee deleted successfully with ID: {}", id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<EmployeeResponse> getEmployeesByDepartment(Department department) {
		log.info("Fetching employees by department: {}", department);
		List<Employee> employees = employeeRepository.findByDepartment(department);
		return employeeMapper.toResponseList(employees);
	}

	@Override
	@Transactional(readOnly = true)
	public List<EmployeeResponse> getEmployeesByStatus(EmployeeStatus status) {
		log.info("Fetching employees by status: {}", status);
		List<Employee> employees = employeeRepository.findByStatus(status);
		return employeeMapper.toResponseList(employees);
	}

	@Override
	@Transactional(readOnly = true)
	public List<EmployeeResponse> getEmployeesByManager(String managerId) {
		log.info("Fetching employees by manager ID: {}", managerId);
		List<Employee> employees = employeeRepository.findByManagerId(managerId);
		return employeeMapper.toResponseList(employees);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<EmployeeResponse> searchEmployees(String keyword, Pageable pageable) {
		log.info("Searching employees with keyword: {}", keyword);
		Page<Employee> employees = employeeRepository.searchEmployees(keyword, pageable);
		return employees.map(employeeMapper::toResponse);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean existsByEmployeeId(String employeeId) {
		return employeeRepository.existsByEmployeeId(employeeId);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean existsByEmail(String email) {
		return employeeRepository.existsByEmail(email);
	}

	@Override
	@Transactional(readOnly = true)
	public long countByDepartment(Department department) {
		return employeeRepository.countByDepartment(department);
	}

	@Override
	@Transactional(readOnly = true)
	public long countByStatus(EmployeeStatus status) {
		return employeeRepository.countByStatus(status);
	}

	@Transactional(readOnly = true)
	public Page<EmployeeResponse> searchEmployeesByDepartment(Department department, Pageable pageable) {
		log.info("Searching employees by department: {}", department);
		Page<Employee> employees = employeeRepository.findByDepartment(department, pageable);
		return employees.map(employeeMapper::toResponse);
	}

}
