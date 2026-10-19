
package com.doritech.microservice.CustomerService.ServiceImpl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.doritech.microservice.CustomerService.Entity.DiwaliCustomer;
import com.doritech.microservice.CustomerService.Repository.DiwaliCustomerRepository;
import com.doritech.microservice.CustomerService.Response.ApiResponse;
import com.doritech.microservice.CustomerService.Response.PageResponse;
import com.doritech.microservice.CustomerService.Service.DiwaliCustomerService;

@Service
public class DiwaliCustomerServiceImpl implements DiwaliCustomerService {

	private final DiwaliCustomerRepository repository;

	public DiwaliCustomerServiceImpl(DiwaliCustomerRepository repository) {
		this.repository = repository;
	}

	// Create customer
	@Override
	@Transactional
	public ResponseEntity<ApiResponse<DiwaliCustomer>> createCustomer(DiwaliCustomer customer) {
		try {
			if (customer == null) {
				return ResponseEntity.badRequest().body(ApiResponse.<DiwaliCustomer>error("Customer data is required"));
			}

			if (isBlank(customer.getCompanyName())) {
				return ResponseEntity.badRequest().body(ApiResponse.<DiwaliCustomer>error("Company name is required"));
			}

			if (isBlank(customer.getContactPersonName())) {
				return ResponseEntity.badRequest()
						.body(ApiResponse.<DiwaliCustomer>error("Contact person name is required"));
			}

			customer.setId(null);
			DiwaliCustomer saved = repository.save(customer);

			return ResponseEntity.status(HttpStatus.CREATED)
					.body(ApiResponse.success("Customer created successfully", saved));

		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(ApiResponse.<DiwaliCustomer>error("Failed to create customer"));
		}
	}

	// Update customer by ID
	@Override
	@Transactional
	public ResponseEntity<ApiResponse<DiwaliCustomer>> updateCustomer(Long id, DiwaliCustomer request) {
		try {
			if (id == null || id <= 0) {
				return ResponseEntity.badRequest()
						.body(ApiResponse.<DiwaliCustomer>error("Valid customer ID is required"));
			}

			if (request == null) {
				return ResponseEntity.badRequest().body(ApiResponse.<DiwaliCustomer>error("Customer data is required"));
			}

			DiwaliCustomer existing = repository.findById(id).orElse(null);

			if (existing == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(ApiResponse.<DiwaliCustomer>error("Customer not found with ID: " + id));
			}

			// Update only the fields supplied in the request.
			if (request.getCompanyName() != null)
				existing.setCompanyName(request.getCompanyName());
			if (request.getContactPersonName() != null)
				existing.setContactPersonName(request.getContactPersonName());
			if (request.getDesignation() != null)
				existing.setDesignation(request.getDesignation());
			if (request.getDepartment() != null)
				existing.setDepartment(request.getDepartment());
			if (request.getEmailId() != null)
				existing.setEmailId(request.getEmailId());
			if (request.getMobileNumber() != null)
				existing.setMobileNumber(request.getMobileNumber());
			if (request.getAlternateNumber() != null)
				existing.setAlternateNumber(request.getAlternateNumber());
			if (request.getLandlineBoardline() != null)
				existing.setLandlineBoardline(request.getLandlineBoardline());
			if (request.getBranchRegionalOffice() != null)
				existing.setBranchRegionalOffice(request.getBranchRegionalOffice());
			if (request.getHeadOfficeAddress() != null)
				existing.setHeadOfficeAddress(request.getHeadOfficeAddress());
			if (request.getCity() != null)
				existing.setCity(request.getCity());
			if (request.getState() != null)
				existing.setState(request.getState());
			if (request.getPincode() != null)
				existing.setPincode(request.getPincode());
			if (request.getCountry() != null)
				existing.setCountry(request.getCountry());

			if (isBlank(existing.getCompanyName())) {
				return ResponseEntity.badRequest().body(ApiResponse.<DiwaliCustomer>error("Company name is required"));
			}

			if (isBlank(existing.getContactPersonName())) {
				return ResponseEntity.badRequest()
						.body(ApiResponse.<DiwaliCustomer>error("Contact person name is required"));
			}

			DiwaliCustomer updated = repository.save(existing);

			return ResponseEntity.ok(ApiResponse.success("Customer updated successfully", updated));

		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(ApiResponse.<DiwaliCustomer>error("Failed to update customer"));
		}
	}

	// Get customer by ID
	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ApiResponse<DiwaliCustomer>> getCustomerById(Long id) {
		try {
			if (id == null || id <= 0) {
				return ResponseEntity.badRequest()
						.body(ApiResponse.<DiwaliCustomer>error("Valid customer ID is required"));
			}

			DiwaliCustomer customer = repository.findById(id).orElse(null);

			if (customer == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(ApiResponse.<DiwaliCustomer>error("Customer not found with ID: " + id));
			}

			return ResponseEntity.ok(ApiResponse.success("Customer fetched successfully", customer));

		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(ApiResponse.<DiwaliCustomer>error("Failed to fetch customer"));
		}
	}

	// Get all customers
	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ApiResponse<List<DiwaliCustomer>>> getAllCustomers() {
		try {
			List<DiwaliCustomer> customers = repository.findAll();

			return ResponseEntity.ok(ApiResponse.success("Customers fetched successfully", customers));

		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(ApiResponse.<List<DiwaliCustomer>>error("Failed to fetch customers"));
		}
	}

	@Override
	public ResponseEntity<ApiResponse<String>> updateCustomersFromExcel(MultipartFile file) {

		try {
			if (file == null || file.isEmpty()) {
				return ResponseEntity.badRequest()
						.body(ApiResponse.<String>error("Please upload a non-empty Excel file"));
			}

			String filename = file.getOriginalFilename();

			if (filename == null || !(filename.toLowerCase(Locale.ROOT).endsWith(".xlsx")
					|| filename.toLowerCase(Locale.ROOT).endsWith(".xls"))) {

				return ResponseEntity.badRequest()
						.body(ApiResponse.<String>error("Only .xlsx and .xls files are allowed"));
			}

			int created = 0;
			int updated = 0;
			int failed = 0;
			int skipped = 0;

			List<String> errors = new ArrayList<>();

			try (InputStream inputStream = file.getInputStream();
					Workbook workbook = WorkbookFactory.create(inputStream)) {

				if (workbook.getNumberOfSheets() == 0) {
					return ResponseEntity.badRequest().body(ApiResponse.<String>error("Excel file contains no sheets"));
				}

				Sheet sheet = workbook.getSheetAt(0);
				Row headerRow = sheet.getRow(0);

				if (headerRow == null) {
					return ResponseEntity.badRequest().body(ApiResponse.<String>error("Excel header row is missing"));
				}

				DataFormatter formatter = new DataFormatter();
				Map<String, Integer> columns = new HashMap<>();

				// Read and normalize Excel headers.
				for (Cell cell : headerRow) {
					String header = normalizeHeader(formatter.formatCellValue(cell));

					if (!header.isEmpty()) {
						columns.put(header, cell.getColumnIndex());
					}
				}

				if (!columns.containsKey("company name")) {
				    return ResponseEntity.badRequest()
				            .body(ApiResponse.<String>error("Excel must contain Company Name column"));
				}

				boolean hasIdColumn = columns.containsKey("id");

				for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {

					Row row = sheet.getRow(rowIndex);

					if (row == null || isRowEmpty(row, formatter)) {
						skipped++;
						continue;
					}

					try {
						DiwaliCustomer customer;
						boolean isNewCustomer;

						String idValue = hasIdColumn ? getCellValue(row, columns.get("id"), formatter) : "";

						if (idValue.isBlank()) {
							// No ID: create a new customer.
							customer = new DiwaliCustomer();
							isNewCustomer = true;
						} else {
							// ID supplied: update the existing customer.
							Long id;

							try {
								id = Long.parseLong(idValue);
							} catch (NumberFormatException ex) {
								throw new IllegalArgumentException("Invalid customer ID: " + idValue);
							}

							customer = repository.findById(id).orElse(null);

							if (customer == null) {
								throw new IllegalArgumentException("Customer not found with ID: " + id);
							}

							isNewCustomer = false;
						}

						// Map Excel columns to entity fields.
						updateField(row, columns, formatter, "company name", customer::setCompanyName);

						updateField(row, columns, formatter, "contact person name", customer::setContactPersonName);

						updateField(row, columns, formatter, "designation, department", customer::setDesignation);

						updateField(row, columns, formatter, "department", customer::setDepartment);

						updateField(row, columns, formatter, "email id", customer::setEmailId);

						updateField(row, columns, formatter, "mobile number", customer::setMobileNumber);

						updateField(row, columns, formatter, "alternate number", customer::setAlternateNumber);

						updateField(row, columns, formatter, "landline / boardline", customer::setLandlineBoardline);

						updateField(row, columns, formatter, "branch / regional office",
								customer::setBranchRegionalOffice);

						updateField(row, columns, formatter, "address (head office)", customer::setHeadOfficeAddress);

						updateField(row, columns, formatter, "city", customer::setCity);

						updateField(row, columns, formatter, "state", customer::setState);

						updateField(row, columns, formatter, "pincode", customer::setPincode);

						updateField(row, columns, formatter, "country", customer::setCountry);

						if (isBlank(customer.getCompanyName())) {
						    throw new IllegalArgumentException("Company Name is required");
						}
						
						// Ensure the database generates the ID for new customers.
						if (isNewCustomer) {
							customer.setId(null);
						}

						repository.saveAndFlush(customer);

						if (isNewCustomer) {
							created++;
						} else {
							updated++;
						}

					} catch (Exception ex) {
						failed++;
						errors.add("Row " + (rowIndex + 1) + ": " + ex.getMessage());
					}
				}
			}

			StringBuilder summary = new StringBuilder("Excel processing completed. Created: ").append(created)
					.append(", Updated: ").append(updated).append(", Failed: ").append(failed).append(", Skipped: ")
					.append(skipped);

			if (!errors.isEmpty()) {
				summary.append(". Errors: ").append(String.join(" | ", errors));
			}

			return ResponseEntity.ok(ApiResponse.success("Excel processing completed", summary.toString()));

		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(ApiResponse.<String>error("Failed to process Excel file"));
		}
	}

	private void updateField(Row row, Map<String, Integer> columns, DataFormatter formatter, String header,
			Consumer<String> setter) {

		Integer columnIndex = columns.get(header);

		if (columnIndex != null) {
			String value = getCellValue(row, columnIndex, formatter);

			// Blank Excel cells leave the existing value unchanged.
			if (!value.isBlank()) {
				setter.accept(value);
			}
		}
	}

	private String getCellValue(Row row, int columnIndex, DataFormatter formatter) {

		Cell cell = row.getCell(columnIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);

		return cell == null ? "" : formatter.formatCellValue(cell).trim();
	}

	private boolean isRowEmpty(Row row, DataFormatter formatter) {
		for (Cell cell : row) {
			if (!formatter.formatCellValue(cell).trim().isEmpty()) {
				return false;
			}
		}
		return true;
	}

	private boolean isBlank(String value) {
		return value == null || value.isBlank();
	}

	private String normalizeHeader(String header) {
		if (header == null) {
			return "";
		}
		return header.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " ");
	}

	@Override
	public ResponseEntity<ApiResponse<PageResponse<DiwaliCustomer>>> getAllCustomersWithPagination(int page, int size) {

		try {
			if (page < 0) {
				return ResponseEntity.badRequest()
						.body(ApiResponse.<PageResponse<DiwaliCustomer>>error("Page number cannot be negative"));
			}

			if (size <= 0) {
				return ResponseEntity.badRequest()
						.body(ApiResponse.<PageResponse<DiwaliCustomer>>error("Page size must be greater than zero"));
			}

			Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

			Page<DiwaliCustomer> customerPage = repository.findAll(pageable);

			PageResponse<DiwaliCustomer> pageResponse = new PageResponse<>(customerPage.getContent(),
					customerPage.getNumber(), customerPage.getSize(), customerPage.getTotalElements(),
					customerPage.getTotalPages(), customerPage.isLast());

			return ResponseEntity.ok(ApiResponse.success("Customers fetched successfully", pageResponse));

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					ApiResponse.<PageResponse<DiwaliCustomer>>error("Failed to fetch customers: " + e.getMessage()));
		}
	}

	@Override
	@Transactional
	public ResponseEntity<ApiResponse<String>> deleteCustomer(Long id) {
	    try {
	        if (id == null || id <= 0) {
	            return ResponseEntity.badRequest()
	                    .body(ApiResponse.<String>error(
	                            "Valid customer ID is required"));
	        }

	        DiwaliCustomer customer = repository.findById(id).orElse(null);

	        if (customer == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body(ApiResponse.<String>error(
	                            "Customer not found with ID: " + id));
	        }

	        repository.delete(customer);

	        return ResponseEntity.ok(
	                ApiResponse.success(
	                        "Customer deleted successfully",
	                        "Deleted customer ID: " + id));

	    } catch (Exception ex) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(ApiResponse.<String>error(
	                        "Failed to delete customer"));
	    }
	}
}
