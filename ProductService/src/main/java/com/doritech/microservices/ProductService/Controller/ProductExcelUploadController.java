package com.doritech.microservices.ProductService.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.doritech.microservices.ProductService.Response.ApiResponse;
import com.doritech.microservices.ProductService.Service.ProductExcelUploadService;

@RestController
@RequestMapping("/product/v1")
@CrossOrigin(allowedHeaders = "*")
public class ProductExcelUploadController {
	private static final Logger log = LoggerFactory.getLogger(ProductController.class);

	@Autowired
	ProductExcelUploadService productExcelUploadService;

	@PostMapping("/uploadExcel")
	public ResponseEntity<ApiResponse<Object>> uploadExcelFile(@RequestParam("file") MultipartFile file) {
		log.info("Request received to upload product Excel file: {}", file.getOriginalFilename());

		ApiResponse<Object> response = productExcelUploadService.uploadProductsFromExcel(file);
		return ResponseEntity.ok(response);
	}

}
