package com.doritech.microservice.CustomerService.ServiceImpl;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.doritech.microservice.CustomerService.Entity.Contact;
import com.doritech.microservice.CustomerService.Entity.Customer;
import com.doritech.microservice.CustomerService.Entity.CustomerAddress;
import com.doritech.microservice.CustomerService.Entity.CustomerTag;
import com.doritech.microservice.CustomerService.Entity.Tag;
import com.doritech.microservice.CustomerService.Enum.CustomerStatus;
import com.doritech.microservice.CustomerService.Exception.GlobalException;
import com.doritech.microservice.CustomerService.Helper.ExcelHelper;
import com.doritech.microservice.CustomerService.Mapper.ContactMapper;
import com.doritech.microservice.CustomerService.Mapper.CustomerAddressMapper;
import com.doritech.microservice.CustomerService.Mapper.CustomerMapper;
import com.doritech.microservice.CustomerService.Repository.ContactRepository;
import com.doritech.microservice.CustomerService.Repository.CustomerAddressRepository;
import com.doritech.microservice.CustomerService.Repository.CustomerRepository;
import com.doritech.microservice.CustomerService.Repository.CustomerTagRepository;
import com.doritech.microservice.CustomerService.Repository.TagRepository;
import com.doritech.microservice.CustomerService.Request.ContactRequest;
import com.doritech.microservice.CustomerService.Request.CustomerAddressRequest;
import com.doritech.microservice.CustomerService.Request.CustomerRequest;
import com.doritech.microservice.CustomerService.Response.ApiResponse;
import com.doritech.microservice.CustomerService.Response.ContactResponse;
import com.doritech.microservice.CustomerService.Response.CustomerResponse;
import com.doritech.microservice.CustomerService.Response.PagedResponse;
import com.doritech.microservice.CustomerService.Service.CustomerService;
import com.doritech.microservice.CustomerService.Service.EmailService;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

	private static final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private TagRepository tagRepository;

	@Autowired
	private CustomerTagRepository customerTagRepository;

	@Autowired
	private CustomerMapper customerMapper;

	@Autowired
	private ContactMapper contactMapper;

	@Autowired
	private CustomerAddressMapper customerAddressMapper;

	@Autowired
	private CustomerGeneratorService customerGeneratorService;

	@Autowired
	private EmailService emailService;

	// Additional dependencies needed for contact and address operations.

	@Autowired
	private ContactRepository contactRepository;

	@Autowired
	private CustomerAddressRepository customerAddressRepository;

	// =========================================================
	// 1. CREATE CUSTOMER
	// =========================================================

	@Override
	public CustomerResponse createCustomer(CustomerRequest request) {

		log.info("Creating customer with GST number: {}", request.getGstNumber());

		if (request.getGstNumber() != null && !request.getGstNumber().isBlank()
				&& customerRepository.existsByGstNumber(request.getGstNumber())) {

			throw new GlobalException("Customer with GST number " + request.getGstNumber() + " already exists");
		}

		// Check whether the first contact email already exists.
		if (request.getContacts() != null && !request.getContacts().isEmpty()) {

			ContactRequest firstContact = request.getContacts().iterator().next();

			String email = firstContact.getEmail();

			if (email != null && !email.isBlank()
					&& customerRepository.existsByContacts_Email(email.trim().toLowerCase())) {

				throw new GlobalException("Customer with email " + email + " already exists");
			}
		}

		// Generate customer code.
		String customerCode;

		try {
			customerCode = customerGeneratorService.updateAndGetCustomerValue(1);
		} catch (Exception e) {
			log.error("Failed to generate customer code", e);
			throw new GlobalException("Could not generate customer code");
		}

		Customer customer = customerMapper.toEntity(request);
		customer.setCustomerCode(customerCode);

		// Build the customer name using branch, organization and city.
		String city = "";

		if (request.getAddresses() != null && !request.getAddresses().isEmpty()) {

			CustomerAddressRequest address = request.getAddresses().iterator().next();

			city = address.getCity() == null ? "" : address.getCity();
		}

		String officeBranch = request.getOfficeBranch() == null ? "" : request.getOfficeBranch();

		String organization = request.getOrganization() == null ? "" : request.getOrganization();

		String mergedName = java.util.stream.Stream.of(officeBranch, organization, city)
				.filter(value -> !value.isBlank()).collect(Collectors.joining(" / "));

		customer.setName(mergedName);

		if (customer.getContacts() != null) {
			customer.getContacts().forEach(contact -> {

				String firstName = contact.getFirstName() == null ? "" : contact.getFirstName();

				String lastName = contact.getLastName() == null ? "" : contact.getLastName();

				contact.setFullName((firstName + " " + lastName).trim());

				if (contact.getEmail() != null) {
					contact.setEmail(contact.getEmail().trim().toLowerCase());
				}

				contact.setCustomer(customer);
			});
		}

		if (customer.getAddresses() != null) {
			customer.getAddresses().forEach(address -> address.setCustomer(customer));
		}

		if (request.getOrganization() != null) {
			customer.setBankName(request.getOrganization());
		}

		// Convert contract dates.
		try {
			if (request.getContractStartDate() != null) {
				customer.setContractStartDate(convertToDate(request.getContractStartDate()));
			}

			if (request.getContractEndDate() != null) {
				customer.setContractEndDate(convertToDate(request.getContractEndDate()));
			}
		} catch (Exception e) {
			log.error("Failed to parse contract dates", e);
			throw new GlobalException("Invalid contract date format");
		}

		Customer savedCustomer = customerRepository.save(customer);

		if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
			addTagsToCustomer(savedCustomer, request.getTagIds());
		}

		// Send welcome email without failing customer creation
		// if the email service encounters an error.
		try {
			if (savedCustomer.getContacts() != null) {

				Optional<String> email = savedCustomer.getContacts().stream().map(Contact::getEmail)
						.filter(value -> value != null && !value.isBlank()).findFirst();

				if (email.isPresent()) {
					emailService.sendWelcomeEmail(email.get(), savedCustomer.getName(),
							savedCustomer.getCustomerCode());

					log.info("Welcome email sent to {}", email.get());
				}
			}
		} catch (Exception e) {
			log.error("Failed to send welcome email", e);
		}

		log.info("Customer created successfully. ID: {}, Code: {}", savedCustomer.getId(), customerCode);

		return customerMapper.toResponse(savedCustomer);
	}

	// =========================================================
	// 2. DATE CONVERSION
	// =========================================================

	private Date convertToDate(Object dateObject) throws ParseException {

		if (dateObject instanceof Date) {
			return (Date) dateObject;
		}

		if (dateObject instanceof String) {

			String[] formats = { "yyyy-MM-dd", "dd/MM/yyyy", "MM/dd/yyyy" };

			for (String format : formats) {
				try {
					SimpleDateFormat dateFormat = new SimpleDateFormat(format);

					dateFormat.setLenient(false);

					return dateFormat.parse((String) dateObject);

				} catch (ParseException ignored) {
					// Try the next supported format.
				}
			}
		}

		throw new ParseException("Cannot parse date: " + dateObject, 0);
	}

	// =========================================================
	// 3. GET CUSTOMER BY ID
	// =========================================================

	@Override
	@Cacheable(value = "customers", key = "#id")
	@Transactional(readOnly = true)
	public CustomerResponse getCustomerById(Long id) {

		Customer customer = customerRepository.findByIdWithDetails(id)
				.orElseThrow(() -> new GlobalException("Customer not found with ID: " + id));

		return customerMapper.toResponse(customer);
	}

	// =========================================================
	// 4. GET CUSTOMER BY CODE
	// =========================================================

	@Override
	@Cacheable(value = "customers", key = "#customerCode")
	@Transactional(readOnly = true)
	public CustomerResponse getCustomerByCode(String customerCode) {

		Customer customer = customerRepository.findByCustomerCode(customerCode)
				.orElseThrow(() -> new GlobalException("Customer not found with code: " + customerCode));

		return customerMapper.toResponse(customer);
	}

	// =========================================================
	// 5. UPDATE CUSTOMER
	// =========================================================

	@Override
	@CacheEvict(value = "customers", key = "#id")
	public CustomerResponse updateCustomer(Long id, CustomerRequest request) {

		Customer customer = customerRepository.findById(id)
				.orElseThrow(() -> new GlobalException("Customer not found with ID: " + id));

		if (request.getGstNumber() != null && !request.getGstNumber().equals(customer.getGstNumber())
				&& customerRepository.existsByGstNumber(request.getGstNumber())) {

			throw new GlobalException("Customer with GST number " + request.getGstNumber() + " already exists");
		}

		customerMapper.updateEntity(customer, request);

		// Replace tags only when tagIds is supplied.
		if (request.getTagIds() != null) {
			customerTagRepository.deleteByCustomerId(customer.getId());
		}

		// Replace contacts when contacts are supplied.
		if (request.getContacts() != null) {

			if (customer.getContacts() != null) {
				customer.getContacts().clear();
			}

			Set<Contact> contacts = request.getContacts().stream().map(contactRequest -> {

				Contact contact = contactMapper.toEntity(contactRequest);

				String firstName = contact.getFirstName() == null ? "" : contact.getFirstName();

				String lastName = contact.getLastName() == null ? "" : contact.getLastName();

				contact.setFullName((firstName + " " + lastName).trim());

				if (contact.getEmail() != null) {
					contact.setEmail(contact.getEmail().trim().toLowerCase());
				}

				contact.setCustomer(customer);

				return contact;
			}).collect(Collectors.toSet());

			customer.setContacts(contacts);
		}

		// Replace addresses when addresses are supplied.
		if (request.getAddresses() != null) {

			if (customer.getAddresses() != null) {
				customer.getAddresses().clear();
			}

			Set<CustomerAddress> addresses = request.getAddresses().stream().map(addressRequest -> {

				CustomerAddress address = customerAddressMapper.toEntity(addressRequest);

				address.setCustomer(customer);

				return address;
			}).collect(Collectors.toSet());

			customer.setAddresses(addresses);
		}

		try {
			if (request.getContractStartDate() != null) {
				customer.setContractStartDate(convertToDate(request.getContractStartDate()));
			}

			if (request.getContractEndDate() != null) {
				customer.setContractEndDate(convertToDate(request.getContractEndDate()));
			}
		} catch (Exception e) {
			log.error("Failed to parse contract dates", e);
			throw new GlobalException("Invalid contract date format");
		}

		Customer updatedCustomer = customerRepository.save(customer);

		if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
			addTagsToCustomer(updatedCustomer, request.getTagIds());
		}

		log.info("Customer updated successfully. ID: {}", updatedCustomer.getId());

		return customerMapper.toResponse(updatedCustomer);
	}

	// =========================================================
	// 6. DELETE CUSTOMER
	// =========================================================

	@Override
	@CacheEvict(value = "customers", key = "#id")
	public void deleteCustomer(Long id) {

		if (!customerRepository.existsById(id)) {
			throw new GlobalException("Customer not found with ID: " + id);
		}

		customerRepository.deleteById(id);

		log.info("Customer deleted successfully. ID: {}", id);
	}

	// =========================================================
	// 7. GET ALL CUSTOMERS WITH PAGINATION
	// =========================================================

	@Override
	@Transactional(readOnly = true)
	public PagedResponse<CustomerResponse> getAllCustomers(Pageable pageable) {

		Page<Customer> page = customerRepository.findAll(pageable);

		return customerMapper.toPagedResponse(page);
	}

	// =========================================================
	// 8. SEARCH CUSTOMERS
	// =========================================================

	@Override
	@Transactional(readOnly = true)
	public PagedResponse<CustomerResponse> searchCustomers(String name, String industry, CustomerStatus status,
			Pageable pageable) {

		Page<Customer> page = customerRepository.findCustomersWithFilters(name, industry, status, pageable);

		return customerMapper.toPagedResponse(page);
	}

	// =========================================================
	// 9. GET CUSTOMERS BY STATUS
	// =========================================================

	@Override
	@Transactional(readOnly = true)
	public List<CustomerResponse> getCustomersByStatus(CustomerStatus status) {

		return customerMapper.toResponseList(customerRepository.findByStatus(status));
	}

	// =========================================================
	// 10. GET CUSTOMERS BY TAG
	// =========================================================

	@Override
	@Transactional(readOnly = true)
	public List<CustomerResponse> getCustomersByTag(Long tagId) {

		return customerMapper.toResponseList(customerRepository.findByTagId(tagId));
	}

	// =========================================================
	// 11. ADD TAG TO CUSTOMER
	// =========================================================

	@Override
	@CacheEvict(value = "customers", key = "#customerId")
	public CustomerResponse addTagToCustomer(Long customerId, Long tagId) {

		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new GlobalException("Customer not found with ID: " + customerId));

		Tag tag = tagRepository.findById(tagId)
				.orElseThrow(() -> new GlobalException("Tag not found with ID: " + tagId));

		if (customerTagRepository.findByCustomerIdAndTagId(customerId, tagId).isPresent()) {

			return customerMapper.toResponse(customer);
		}

		CustomerTag customerTag = new CustomerTag();
		customerTag.setCustomer(customer);
		customerTag.setTag(tag);

		customerTagRepository.save(customerTag);

		Customer refreshedCustomer = customerRepository.findByIdWithDetails(customerId).orElse(customer);

		return customerMapper.toResponse(refreshedCustomer);
	}

	// =========================================================
	// 12. REMOVE TAG FROM CUSTOMER
	// =========================================================

	@Override
	@CacheEvict(value = "customers", key = "#customerId")
	public void removeTagFromCustomer(Long customerId, Long tagId) {

		CustomerTag customerTag = customerTagRepository.findByCustomerIdAndTagId(customerId, tagId)
				.orElseThrow(() -> new GlobalException("Tag association not found"));

		customerTagRepository.delete(customerTag);
	}

	// =========================================================
	// 13. COUNT CUSTOMERS BY STATUS
	// =========================================================

	@Override
	@Transactional(readOnly = true)
	public long getCustomerCountByStatus(CustomerStatus status) {

		return customerRepository.countByStatus(status);
	}

	// =========================================================
	// 14. ADD TAGS HELPER
	// =========================================================

	private void addTagsToCustomer(Customer customer, List<Long> tagIds) {

		List<Tag> tags = tagRepository.findAllById(tagIds);

		if (tags.size() != tagIds.size()) {
			throw new GlobalException("One or more tags not found");
		}

		List<CustomerTag> customerTags = new ArrayList<>();

		for (Tag tag : tags) {

			CustomerTag customerTag = new CustomerTag();
			customerTag.setCustomer(customer);
			customerTag.setTag(tag);

			customerTags.add(customerTag);
		}

		customerTagRepository.saveAll(customerTags);
	}

	// =========================================================
	// 15. SAVE CUSTOMER ASYNCHRONOUSLY
	// =========================================================

	@Override
	@Async("taskExecutor")
	public CompletableFuture<Void> saveCustomerAsync(Customer customer) {

		try {
			customerRepository.save(customer);

			return CompletableFuture.completedFuture(null);

		} catch (Exception e) {

			log.error("Failed to save customer: {}", customer.getName(), e);

			return CompletableFuture.failedFuture(e);
		}
	}

	// =========================================================
	// 16. PROCESS UPLOADED EXCEL
	// =========================================================

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void processUploadedExcel(MultipartFile file) throws Exception {

		if (file == null || file.isEmpty()) {
			throw new GlobalException("Please upload a valid Excel file");
		}

		// Parse the Excel file
		List<Customer> customers = ExcelHelper.parseExcel(file, customerGeneratorService);

		if (customers == null || customers.isEmpty()) {
			throw new GlobalException("No valid customers found in Excel file");
		}

		// Generate a unique customer code for each customer
		for (Customer customer : customers) {
			String code = customerGeneratorService.updateAndGetCustomerValue(1);

			customer.setCustomerCode(code);
		}

		// Save all customers
		saveCustomersBatch(customers);
	}

	// =========================================================
	// 17. SAVE CUSTOMERS IN BATCHES
	// =========================================================

	@Override
	@Transactional
	public void saveCustomersBatch(List<Customer> customers) {

		final int BATCH_SIZE = 100;

		for (int i = 0; i < customers.size(); i += BATCH_SIZE) {

			int end = Math.min(i + BATCH_SIZE, customers.size());

			List<Customer> batch = customers.subList(i, end);

			customerRepository.saveAll(batch);

			log.info("Saved batch {} with {} customers", (i / BATCH_SIZE) + 1, batch.size());
		}
	}

	// =========================================================
	// 18. GET ALL CUSTOMERS WITHOUT PAGINATION
	// =========================================================

	@Override
	@Transactional(readOnly = true)
	public List<CustomerResponse> getAllCustomers() {

		return customerMapper.toResponseList(customerRepository.findAll());
	}

	// =========================================================
	// 19. GET CUSTOMER ID AND NAME
	// =========================================================

	public List<Map<String, Object>> getCustomerIdAndNameList() {

		List<Customer> customers = customerRepository.findAll();

		List<Map<String, Object>> result = new ArrayList<>();

		for (Customer customer : customers) {

			Map<String, Object> map = new HashMap<>();

			map.put("id", customer.getId());
			map.put("name", customer.getName());

			result.add(map);
		}

		return result;
	}

	// =========================================================
	// 20. GET CONTACT BY EMAIL
	// =========================================================

	public ContactResponse getContactByEmail(String email) {

		Contact contact = contactRepository.findByEmail(email)
				.orElseThrow(() -> new GlobalException("Contact not found with email: " + email));

		ContactResponse response = new ContactResponse();

		response.setId(contact.getId());
		response.setFullName(contact.getFullName());
		response.setMobileNumber(contact.getMobileNumber());
		response.setDesignation(contact.getDesignation());
		response.setEmail(contact.getEmail());

		return response;
	}

	// =========================================================
	// 21. GET CUSTOMERS BY ORGANIZATION
	// =========================================================

	public ApiResponse<List<Map<String, Object>>> getCustomersByOrganization(String organization) {

		List<Customer> customers = customerRepository.findByOrganization(organization);

		if (customers.isEmpty()) {
			return ApiResponse.error("No customers found for organization: " + organization);
		}

		List<Map<String, Object>> result = customers.stream().map(customer -> {

			Map<String, Object> map = new HashMap<>();

			map.put("id", customer.getId());
			map.put("name", customer.getName());
			map.put("customerCode", customer.getCustomerCode());

			return map;
		}).collect(Collectors.toList());

		return ApiResponse.success("Fetched customers by organization", result);
	}

	// =========================================================
	// 22. DOWNLOAD CUSTOMER EXCEL TEMPLATE
	// =========================================================

	public ResponseEntity<?> downloadCustomerTemplate() {

		String[] headers = { "Officebranch", "Zonename", "Branch", "Ifsc", "City", "Region", "Faname", "EMAIL ID",
				"Organization", "Hierarchy", "Site Type", "Legal Name", "Industry", "Customer Status", "First Name",
				"Last Name", "Designation", "Department", "Mobile Number", "Alternate Number", "Landline", "Role",
				"Is Primary Contact", "Gender", "Address Line 1", "Address Line 2", "State", "Pincode", "Country",
				"Address Type", "GST", "Website", "Service Used", "Annual Budget", "Contact Type",
				"Preferred Communication Mode", "Remarks" };

		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream output = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.createSheet("Customer Template");

			Row headerRow = sheet.createRow(0);

			CellStyle headerStyle = workbook.createCellStyle();
			Font font = workbook.createFont();
			font.setBold(true);
			headerStyle.setFont(font);

			for (int i = 0; i < headers.length; i++) {

				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);
				cell.setCellStyle(headerStyle);

				sheet.autoSizeColumn(i);
			}

			workbook.write(output);

			ByteArrayResource resource = new ByteArrayResource(output.toByteArray());

			return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=CustomerTemplate.xlsx")
					.contentType(MediaType.APPLICATION_OCTET_STREAM).contentLength(resource.contentLength())
					.body(resource);

		} catch (Exception e) {

			log.error("Failed to generate customer template", e);

			return ResponseEntity.internalServerError()
					.body(ApiResponse.error("Failed to generate Customer Excel template"));
		}
	}

	// =========================================================
	// 23. FILTER CUSTOMERS
	// =========================================================

	public Page<CustomerResponse> getCustomersByFilter(String officeBranch, String bankName, String zoneName,
			String ifsc, Pageable pageable) {

		Page<Customer> page = customerRepository.filterCustomers(officeBranch, bankName, zoneName, ifsc, pageable);

		return page.map(customerMapper::toResponse);
	}

	// =========================================================
	// 24. GET OFFICE BRANCHES
	// =========================================================

	public Set<String> getOfficeBranches() {

		return customerRepository.findAllOfficeBranches();
	}

	// =========================================================
	// 25. GET BANK NAMES BY OFFICE BRANCH
	// =========================================================

	public Set<String> getBankNamesByOfficeBranch(String officeBranch) {

		return customerRepository.findBankNamesByOfficeBranch(officeBranch);
	}

	// =========================================================
	// 26. GET ZONES
	// =========================================================

	public Set<String> getZoneNames(String officeBranch, String bankName) {

		return customerRepository.findZoneNames(officeBranch, bankName);
	}

	// =========================================================
	// 27. GET EMAILS BY BRANCH, BANK AND ZONE
	// =========================================================

	public List<Map<String, String>> getEmails(String officeBranch, String bankName, String zoneName) {

		List<Object[]> results = customerRepository.findEmailsWithBranch(officeBranch, bankName, zoneName);

		List<Map<String, String>> result = new ArrayList<>();

		for (Object[] row : results) {

			Map<String, String> map = new HashMap<>();

			map.put("email", (String) row[0]);
			map.put("branch", (String) row[1]);

			result.add(map);
		}

		return result;
	}

	// =========================================================
	// 28. GET CUSTOMER BY IFSC
	// =========================================================

	public ApiResponse<CustomerResponse> getDetailsByIfsc(String ifsc) {

		Optional<Customer> customer = customerRepository.findByIfsc(ifsc);

		if (customer.isEmpty()) {
			return ApiResponse.error("Customer not found");
		}

		CustomerResponse response = customerMapper.toResponse(customer.get());

		return ApiResponse.success("Customer found", response);
	}

	// =========================================================
	// 29. GET CUSTOMER DETAILS BY EMAIL
	// =========================================================

	public Customer getDetailsByEmail(String email) {

		Customer customer = customerRepository.findByContactEmail(email);

		if (customer == null) {
			throw new GlobalException("Customer not found");
		}

		// Return only the fields needed by this lookup.
		Customer response = new Customer();

		response.setId(customer.getId());
		response.setCustomerCode(customer.getCustomerCode());
		response.setName(customer.getName());

		return response;
	}
}
