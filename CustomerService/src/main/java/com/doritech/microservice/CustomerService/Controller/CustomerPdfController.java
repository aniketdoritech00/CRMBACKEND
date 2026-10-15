package com.doritech.microservice.CustomerService.Controller;

import com.doritech.microservice.CustomerService.ServiceImpl.CustomerPdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
@CrossOrigin(allowedHeaders = "*")
public class CustomerPdfController {

    @Autowired
    private CustomerPdfService customerPdfService;

    /**
     * Download PDF for all customers
     * URL: GET /api/pdf/customers/download
     */
    @GetMapping("/customers/download")
    public ResponseEntity<byte[]> downloadAllCustomersPdf() {
        try {
            byte[] pdfBytes = customerPdfService.generateCustomerListPdf();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "customers-report.pdf");
            headers.setContentLength(pdfBytes.length);
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Download PDF for specific customer
     * URL: GET /api/pdf/customer/{customerId}/download
     */
    @GetMapping("/customer/{customerCode}/download")
    public ResponseEntity<byte[]> downloadCustomerPdf(@PathVariable String customerCode) {
        try {
            byte[] pdfBytes = customerPdfService.generateCustomerPdf(customerCode);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "customer-" + customerCode + "-report.pdf");
            headers.setContentLength(pdfBytes.length);
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * View PDF in browser for all customers
     * URL: GET /api/pdf/customers/view
     */
    @GetMapping("/customers/view")
    public ResponseEntity<byte[]> viewAllCustomersPdf() {
        try {
            byte[] pdfBytes = customerPdfService.generateCustomerListPdf();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentLength(pdfBytes.length);
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/customer/{customerCode}/view")
    public ResponseEntity<byte[]> viewCustomerPdf(@PathVariable String customerCode) {
        try {
            byte[] pdfBytes = customerPdfService.generateCustomerPdf(customerCode);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentLength(pdfBytes.length);
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}