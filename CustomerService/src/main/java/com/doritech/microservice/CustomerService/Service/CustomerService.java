
package com.doritech.microservice.CustomerService.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.doritech.microservice.CustomerService.Entity.Customer;
import com.doritech.microservice.CustomerService.Enum.CustomerStatus;
import com.doritech.microservice.CustomerService.Request.CustomerRequest;
import com.doritech.microservice.CustomerService.Response.ApiResponse;
import com.doritech.microservice.CustomerService.Response.ContactResponse;
import com.doritech.microservice.CustomerService.Response.CustomerResponse;
import com.doritech.microservice.CustomerService.Response.PagedResponse;

public interface CustomerService {

    // 1. Create Customer
    CustomerResponse createCustomer(CustomerRequest request);

    // 2. Get Customer By ID
    CustomerResponse getCustomerById(Long id);

    // 3. Get Customer By Code
    CustomerResponse getCustomerByCode(String customerCode);

    // 4. Update Customer
    CustomerResponse updateCustomer(Long id, CustomerRequest request);

    // 5. Delete Customer
    void deleteCustomer(Long id);

    // 6. Get All Customers With Pagination
    PagedResponse<CustomerResponse> getAllCustomers(Pageable pageable);

    // 7. Search Customers
    PagedResponse<CustomerResponse> searchCustomers(
            String name,
            String industry,
            CustomerStatus status,
            Pageable pageable);

    // 8. Get Customers By Status
    List<CustomerResponse> getCustomersByStatus(CustomerStatus status);

    // 9. Get Customers By Tag
    List<CustomerResponse> getCustomersByTag(Long tagId);

    // 10. Add Tag To Customer
    CustomerResponse addTagToCustomer(Long customerId, Long tagId);

    // 11. Remove Tag From Customer
    void removeTagFromCustomer(Long customerId, Long tagId);

    // 12. Get Customer Count By Status
    long getCustomerCountByStatus(CustomerStatus status);

    // 13. Save Customer Asynchronously
    CompletableFuture<Void> saveCustomerAsync(Customer customer);

    // 14. Save Customers In Batch
    void saveCustomersBatch(List<Customer> customers);

    // 15. Get All Customers Without Pagination
    List<CustomerResponse> getAllCustomers();

    // 16. Get Customer ID And Name List
    List<Map<String, Object>> getCustomerIdAndNameList();

    // 17. Get Contact By Email
    ContactResponse getContactByEmail(String email);

    // 18. Process Uploaded Excel File
    void processUploadedExcel(MultipartFile file) throws Exception;

    // 19. Get Customers By Organization
    ApiResponse<List<Map<String, Object>>> getCustomersByOrganization(
            String organization);

    // 20. Download Customer Excel Template
    ResponseEntity<?> downloadCustomerTemplate();

    // 21. Get Customers By Filter
    Object getCustomersByFilter(
            String officeBranch,
            String bankName,
            String zoneName,
            String ifsc,
            Pageable pageable);

    // 22. Get Office Branches
    Object getOfficeBranches();

    // 23. Get Bank Names By Office Branch
    Object getBankNamesByOfficeBranch(String officeBranch);

    // 24. Get Zone Names
    Object getZoneNames(String officeBranch, String bankName);

    // 25. Get Details By IFSC
    Object getDetailsByIfsc(String ifsc);

    // 26. Get Emails
    Object getEmails(
            String officeBranch,
            String bankName,
            String zoneName);

    // 27. Get Customer Details By Email
    Customer getDetailsByEmail(String email);
}
