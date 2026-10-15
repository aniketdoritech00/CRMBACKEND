package com.doritech.microservice.CustomerService.Controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doritech.microservice.CustomerService.Service.DiwaliCustomerPdfService;

@RestController
@RequestMapping("/api/diwali-customers")
@CrossOrigin(allowedHeaders = "*")
public class DiwaliCustomerPdfController {

	private final DiwaliCustomerPdfService diwaliCustomerPdfService;

	DiwaliCustomerPdfController(DiwaliCustomerPdfService diwaliCustomerPdfService) {
		this.diwaliCustomerPdfService = diwaliCustomerPdfService;
	}

	@GetMapping(value = "/{id}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<byte[]> generateCustomerPdf(@PathVariable Long id) {

		try {
			byte[] pdf = diwaliCustomerPdfService.generateCustomerPdf(id);

			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=diwali-customer-" + id + ".pdf")
					.contentType(MediaType.APPLICATION_PDF).contentLength(pdf.length).body(pdf);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.TEXT_PLAIN)
					.body(("Failed to generate customer PDF: " + e.getMessage())
							.getBytes(java.nio.charset.StandardCharsets.UTF_8));
		}
	}

	@GetMapping(value = "/pdf/all", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<byte[]> generateCustomerListPdf() {

		try {
			byte[] pdf = diwaliCustomerPdfService.generateCustomerListPdf();

			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=diwali-customers.pdf")
					.contentType(MediaType.APPLICATION_PDF).contentLength(pdf.length).body(pdf);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.TEXT_PLAIN)
					.body(("Failed to generate customer list PDF: " + e.getMessage())
							.getBytes(java.nio.charset.StandardCharsets.UTF_8));
		}
	}
}
