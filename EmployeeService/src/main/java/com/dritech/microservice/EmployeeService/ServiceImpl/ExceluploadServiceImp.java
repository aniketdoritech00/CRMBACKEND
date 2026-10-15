package com.dritech.microservice.EmployeeService.ServiceImpl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dritech.microservice.EmployeeService.Entity.Employee;
import com.dritech.microservice.EmployeeService.Enum.Department;
import com.dritech.microservice.EmployeeService.Enum.EmployeeStatus;
import com.dritech.microservice.EmployeeService.Enum.Position;
import com.dritech.microservice.EmployeeService.Repository.EmployeeRepository;
import com.dritech.microservice.EmployeeService.Service.ExceluploadService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ExceluploadServiceImp implements ExceluploadService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public ResponseEntity<?> uploadEmployeesFromExcel(MultipartFile file) {
		List<String> errors = new ArrayList<>();
		List<Employee> employees = new ArrayList<>();

		if (file.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");
		}

		if (!file.getOriginalFilename().endsWith(".xlsx")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Only .xlsx files are supported");
		}

		try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
			Sheet sheet = workbook.getSheetAt(0);
			Row headerRow = sheet.getRow(0);

			if (headerRow == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Excel file must have a header row");
			}

			Map<String, Integer> headerMap = new HashMap<>();
			for (Cell cell : headerRow) {
				headerMap.put(cell.getStringCellValue().trim().toLowerCase(), cell.getColumnIndex());
			}

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (row == null)
					continue;

				try {
					Employee emp = new Employee();
					emp.setFirstName(getString(row, headerMap, "first name"));
					emp.setLastName(getString(row, headerMap, "last name"));
					emp.setName(emp.getFirstName() + " " + emp.getLastName());
					emp.setEmail(getString(row, headerMap, "email"));
					emp.setPhoneNumber(getString(row, headerMap, "phone number"));
					emp.setEmployeeId(getString(row, headerMap, "employee id"));

					String deptStr = getString(row, headerMap, "department");
					String posStr = getString(row, headerMap, "position");
					String statusStr = getString(row, headerMap, "status");

					emp.setDepartment(Department.valueOf(deptStr.toUpperCase()));
					emp.setPosition(Position.valueOf(posStr.toUpperCase()));
					emp.setStatus(EmployeeStatus.valueOf(statusStr.toUpperCase()));

					emp.setAddress(getString(row, headerMap, "address"));
					emp.setManagerId(getString(row, headerMap, "manager id"));
					emp.setProjects(getString(row, headerMap, "projects"));
					emp.setRole(getString(row, headerMap, "role"));

					String rawPassword = getString(row, headerMap, "password");
					emp.setPassword(passwordEncoder.encode(rawPassword));
					emp.setSalary(getDouble(row, headerMap, "salary"));

					if (emp.getFirstName() == null || emp.getEmail() == null || emp.getEmployeeId() == null) {
						errors.add("Row " + (i + 1) + ": Missing mandatory fields");
						continue;
					}

					if (employeeRepository.existsByEmail(emp.getEmail())
							|| employeeRepository.existsByEmployeeId(emp.getEmployeeId())) {
						errors.add("Row " + (i + 1) + ": Duplicate email or employee ID");
						continue;
					}

					employees.add(emp);
				} catch (Exception e) {
					errors.add("Row " + (i + 1) + ": " + e.getMessage());
				}
			}

			if (employees.isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No valid employees found: " + errors);
			}

			employeeRepository.saveAll(employees);

			Map<String, Object> response = new HashMap<>();
			response.put("successCount", employees.size());
			response.put("errors", errors);
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error reading Excel file: " + e.getMessage());
		}
	}

	private String getString(Row row, Map<String, Integer> headerMap, String headerName) {
		Integer index = headerMap.get(headerName.toLowerCase());
		if (index == null)
			return null;
		Cell cell = row.getCell(index);
		if (cell == null)
			return null;
		cell.setCellType(CellType.STRING);
		return cell.getStringCellValue().trim();
	}

	private Double getDouble(Row row, Map<String, Integer> headerMap, String headerName) {
		Integer index = headerMap.get(headerName.toLowerCase());
		if (index == null)
			return null;
		Cell cell = row.getCell(index);
		if (cell == null)
			return null;
		try {
			return cell.getNumericCellValue();
		} catch (Exception e) {
			return null;
		}
	}
}