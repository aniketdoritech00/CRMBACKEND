package com.doritech.microservices.ProductService.Service;

import org.springframework.web.multipart.MultipartFile;

import com.doritech.microservices.ProductService.Response.ApiResponse;

public interface ProductExcelUploadService {


	ApiResponse<Object> uploadProductsFromExcel(MultipartFile file);

}
