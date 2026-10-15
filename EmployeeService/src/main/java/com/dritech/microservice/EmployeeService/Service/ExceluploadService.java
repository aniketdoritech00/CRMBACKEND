package com.dritech.microservice.EmployeeService.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ExceluploadService {
	ResponseEntity<?> uploadEmployeesFromExcel(MultipartFile file);
}