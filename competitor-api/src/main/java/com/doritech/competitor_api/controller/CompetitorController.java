package com.doritech.competitor_api.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.doritech.competitor_api.request.CreateCompetitorRequest;
import com.doritech.competitor_api.request.UpdateCompetitorRequest;
import com.doritech.competitor_api.response.CompetitorListResponse;
import com.doritech.competitor_api.response.CompetitorResponse;
import com.doritech.competitor_api.service.CompetitorService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/competitor")
@CrossOrigin(allowedHeaders = "*")
@Validated
public class CompetitorController {

	private static final Logger logger = LoggerFactory.getLogger(CompetitorController.class);

	private final CompetitorService competitorService;

	public CompetitorController(CompetitorService competitorService) {
		this.competitorService = competitorService;
	}

	@PostMapping
	public ResponseEntity<CompetitorResponse> createCompetitor(@Valid @RequestBody CreateCompetitorRequest request) {
		logger.info("REST request to create competitor: {}", request.getName());
		CompetitorResponse response = competitorService.createCompetitor(request);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<CompetitorResponse> getCompetitorById(@PathVariable @Min(1) Long id) {
		logger.info("REST request to get competitor: {}", id);
		CompetitorResponse response = competitorService.getCompetitorById(id);
		return ResponseEntity.ok(response);
	}

	@GetMapping
	public ResponseEntity<CompetitorListResponse> getAllCompetitors(@RequestParam(defaultValue = "0") @Min(0) int page,
			@RequestParam(defaultValue = "20") @Min(1) int size, @RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "asc") String sortDir) {

		logger.info("REST request to get all competitors: page={}, size={}, sortBy={}, sortDir={}", page, size, sortBy,
				sortDir);

		Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
		Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

		CompetitorListResponse response = competitorService.getAllCompetitors(pageable);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{id}")
	public ResponseEntity<CompetitorResponse> updateCompetitor(@PathVariable @Min(1) Long id,
			@Valid @RequestBody UpdateCompetitorRequest request) {

		logger.info("REST request to update competitor: {}", id);
		CompetitorResponse response = competitorService.updateCompetitor(id, request);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCompetitor(@PathVariable @Min(1) Long id) {
		logger.info("REST request to delete competitor: {}", id);
		competitorService.deleteCompetitor(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/search")
	public ResponseEntity<CompetitorListResponse> searchCompetitors(@RequestParam @NotBlank String q,
			@RequestParam(defaultValue = "0") @Min(0) int page, @RequestParam(defaultValue = "20") @Min(1) int size,
			@RequestParam(defaultValue = "name") String sortBy, @RequestParam(defaultValue = "asc") String sortDir) {

		logger.info("REST request to search competitors: q={}, page={}, size={}", q, page, size);

		Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
		Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

		CompetitorListResponse response = competitorService.searchCompetitors(q, pageable);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/by-industry/{industry}")
	public ResponseEntity<CompetitorListResponse> getCompetitorsByIndustry(@PathVariable @NotBlank String industry,
			@RequestParam(defaultValue = "0") @Min(0) int page, @RequestParam(defaultValue = "20") @Min(1) int size,
			@RequestParam(defaultValue = "name") String sortBy, @RequestParam(defaultValue = "asc") String sortDir) {

		logger.info("REST request to get competitors by industry: {}", industry);

		Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
		Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

		CompetitorListResponse response = competitorService.getCompetitorsByIndustry(industry, pageable);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/industries")
	public ResponseEntity<List<String>> getAllIndustries() {
		logger.info("REST request to get all industries");
		List<String> industries = competitorService.getAllIndustries();
		return ResponseEntity.ok(industries);
	}

	@GetMapping("/exists")
	public ResponseEntity<Boolean> checkCompetitorExists(@RequestParam @NotBlank String name) {
		logger.info("REST request to check if competitor exists: {}", name);
		boolean exists = competitorService.existsByName(name);
		return ResponseEntity.ok(exists);
	}

	@PostMapping("/upload-excel")
	public ResponseEntity<String> uploadExcel(@RequestParam("file") MultipartFile file) {
		if (file.isEmpty()) {
			return ResponseEntity.badRequest().body("Please upload a valid Excel file.");
		}
		try {
			competitorService.uploadCompetitorsFromExcel(file);
			return ResponseEntity.ok("File uploaded and data saved successfully.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error while processing file: " + e.getMessage());
		}
	}
}
