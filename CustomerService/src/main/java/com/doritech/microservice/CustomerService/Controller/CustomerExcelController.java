package com.doritech.microservice.CustomerService.Controller;

import com.doritech.microservice.CustomerService.Entity.Customer;
import com.doritech.microservice.CustomerService.Helper.ExcelHelper;
import com.doritech.microservice.CustomerService.Response.ApiResponse;
import com.doritech.microservice.CustomerService.Service.CustomerService;
import com.doritech.microservice.CustomerService.ServiceImpl.CustomerGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/customers")
@CrossOrigin(allowedHeaders = "*")
public class CustomerExcelController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerGeneratorService customerGeneratorService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<Void>> uploadExcel(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("File is empty"));
        }

        try {
            List<Customer> customers = ExcelHelper.parseExcel(file, customerGeneratorService);

            if (customers.isEmpty()) {
                return ResponseEntity.ok(ApiResponse.success("No valid customers found in the file!", null));
            }

            for (int i = 0; i < customers.size(); i++) {
                try {
                    customerGeneratorService.updateAndGetCustomerValue(1); 
                } catch (Exception e) {
                    System.err.println("Failed to update generator for customer " + (i + 1) + ": " + e.getMessage());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(ApiResponse.error("Failed to update customer generator: " + e.getMessage()));
                }
            }

            customers.forEach(customerService::saveCustomerAsync);

            return ResponseEntity.ok(ApiResponse.success(
                    "File uploaded successfully! " + customers.size() + " customers are being processed.", 
                    null));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to process file: " + e.getMessage()));
        }
    }

    @PostMapping("/upload-batch")
    public ResponseEntity<ApiResponse<Void>> uploadExcelBatch(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("File is empty"));
        }

        try {
            List<Customer> customers = ExcelHelper.parseExcel(file, customerGeneratorService);

            if (customers.isEmpty()) {
                return ResponseEntity.ok(ApiResponse.success("No valid customers found in the file!", null));
            }

            for (int i = 0; i < customers.size(); i++) {
                try {
                    customerGeneratorService.updateAndGetCustomerValue(1); // Replace 1 with appropriate ID
                } catch (Exception e) {
                    System.err.println("Failed to update generator for customer " + (i + 1) + ": " + e.getMessage());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(ApiResponse.error("Failed to update customer generator: " + e.getMessage()));
                }
            }

            customerService.saveCustomersBatch(customers);

            return ResponseEntity.ok(ApiResponse.success(
                    "File uploaded and processed successfully! " + customers.size() + " customers saved.", 
                    null));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to process file: " + e.getMessage()));
        }
    }
}