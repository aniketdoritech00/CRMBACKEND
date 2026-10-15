package com.dritech.microservice.EmployeeService.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dritech.microservice.EmployeeService.Service.ExceluploadService;

@RestController
@RequestMapping("/employee/excel")
public class ExceluploadController {

	private final ExceluploadService exceluploadService;

	ExceluploadController(ExceluploadService exceluploadService) {
		this.exceluploadService = exceluploadService;
	}

	@PostMapping("/upload")
	public ResponseEntity<?> uploadEmployeeExcel(@RequestParam("file") MultipartFile file) {
		return exceluploadService.uploadEmployeesFromExcel(file);
	}
}