
package com.doritech.microservice.CustomerService.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.doritech.microservice.CustomerService.Entity.Customer;
import com.doritech.microservice.CustomerService.Enum.CustomerStatus;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // 1. Find customer by customer code
    Optional<Customer> findByCustomerCode(String customerCode);

    // 2. Check whether customer code already exists
    boolean existsByCustomerCode(String customerCode);

    // 3. Find customers by status
    List<Customer> findByStatus(CustomerStatus status);

    // 4. Find customers by industry
    List<Customer> findByIndustry(String industry);

    // 5. Search customers by name, industry, and status with pagination
    @Query("SELECT c FROM Customer c WHERE "
            + "(:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))) "
            + "AND (:industry IS NULL OR c.industry = :industry) "
            + "AND (:status IS NULL OR c.status = :status)")
    Page<Customer> findCustomersWithFilters(
            @Param("name") String name,
            @Param("industry") String industry,
            @Param("status") CustomerStatus status,
            Pageable pageable);

    // 6. Find customer by ID with addresses, contacts, and customer tags
    @Query("SELECT DISTINCT c FROM Customer c "
            + "LEFT JOIN FETCH c.addresses "
            + "LEFT JOIN FETCH c.contacts "
            + "LEFT JOIN FETCH c.customerTags "
            + "WHERE c.id = :customerId")
    Optional<Customer> findByIdWithDetail(
            @Param("customerId") Long customerId);

    // 7. Find customer by ID with tags and tag details
    @Query("SELECT DISTINCT c FROM Customer c "
            + "LEFT JOIN FETCH c.addresses "
            + "LEFT JOIN FETCH c.contacts "
            + "LEFT JOIN FETCH c.customerTags ct "
            + "LEFT JOIN FETCH ct.tag "
            + "WHERE c.id = :id")
    Optional<Customer> findByIdWithDetails(
            @Param("id") Long id);

    // 8. Find customers associated with a tag
    @Query("SELECT c FROM Customer c "
            + "JOIN c.customerTags ct "
            + "WHERE ct.tag.id = :tagId")
    List<Customer> findByTagId(
            @Param("tagId") Long tagId);

    // 9. Count customers by status
    @Query("SELECT COUNT(c) FROM Customer c WHERE c.status = :status")
    long countByStatus(
            @Param("status") CustomerStatus status);

    // 10. Check whether GST number already exists
    boolean existsByGstNumber(String gstNumber);

    // 11. Find all customers with addresses and contacts
    @Query("SELECT DISTINCT c FROM Customer c "
            + "LEFT JOIN FETCH c.addresses "
            + "LEFT JOIN FETCH c.contacts")
    List<Customer> findAllWithAddressesAndContacts();

    // 12. Find customer by ID with addresses and contacts
    @Query("SELECT DISTINCT c FROM Customer c "
            + "LEFT JOIN FETCH c.addresses "
            + "LEFT JOIN FETCH c.contacts "
            + "WHERE c.id = :id")
    Optional<Customer> findByIdWithAddressesAndContacts(
            @Param("id") Long id);

    // 13. Find customers by organization
    List<Customer> findByOrganization(String organization);

    // 14. Filter customers by office branch, bank, zone, and IFSC
    @Query("SELECT c FROM Customer c WHERE "
            + "(:officeBranch IS NULL OR :officeBranch = '' "
            + "OR c.officeBranch = :officeBranch) "
            + "AND (:bankName IS NULL OR :bankName = '' "
            + "OR c.bankName = :bankName) "
            + "AND (:zoneName IS NULL OR :zoneName = '' "
            + "OR c.zoneName = :zoneName) "
            + "AND (:ifsc IS NULL OR :ifsc = '' "
            + "OR c.ifsc = :ifsc)")
    Page<Customer> filterCustomers(
            @Param("officeBranch") String officeBranch,
            @Param("bankName") String bankName,
            @Param("zoneName") String zoneName,
            @Param("ifsc") String ifsc,
            Pageable pageable);

    // 15. Get all distinct office branches
    @Query("SELECT DISTINCT c.officeBranch FROM Customer c "
            + "WHERE c.officeBranch IS NOT NULL")
    Set<String> findAllOfficeBranches();

    // 16. Get bank names for an office branch
    @Query("SELECT DISTINCT c.bankName FROM Customer c "
            + "WHERE c.bankName IS NOT NULL "
            + "AND (:officeBranch IS NULL OR :officeBranch = '' "
            + "OR c.officeBranch = :officeBranch)")
    Set<String> findBankNamesByOfficeBranch(
            @Param("officeBranch") String officeBranch);

    // 17. Get zone names for an office branch and bank
    @Query("SELECT DISTINCT c.zoneName FROM Customer c "
            + "WHERE c.zoneName IS NOT NULL "
            + "AND (:officeBranch IS NULL OR :officeBranch = '' "
            + "OR c.officeBranch = :officeBranch) "
            + "AND (:bankName IS NULL OR :bankName = '' "
            + "OR c.bankName = :bankName)")
    Set<String> findZoneNames(
            @Param("officeBranch") String officeBranch,
            @Param("bankName") String bankName);

    // 18. Get contact emails and their customer's branch
    @Query("SELECT DISTINCT ct.email, cust.branch "
            + "FROM Contact ct "
            + "JOIN ct.customer cust "
            + "WHERE ct.email IS NOT NULL "
            + "AND (:officeBranch IS NULL OR :officeBranch = '' "
            + "OR cust.officeBranch = :officeBranch) "
            + "AND (:bankName IS NULL OR :bankName = '' "
            + "OR cust.bankName = :bankName) "
            + "AND (:zoneName IS NULL OR :zoneName = '' "
            + "OR cust.zoneName = :zoneName)")
    List<Object[]> findEmailsWithBranch(
            @Param("officeBranch") String officeBranch,
            @Param("bankName") String bankName,
            @Param("zoneName") String zoneName);

    // 19. Check whether IFSC already exists
    boolean existsByIfsc(String ifsc);

    // 20. Find customer by IFSC
    Optional<Customer> findByIfsc(String ifsc);

    // 21. Find customer by contact email
    Optional<Customer> findByContacts_Email(String emailId);

    // 22. Check whether a contact email already exists
    boolean existsByContacts_Email(String email);

    // 23. Find customer using a contact's email
    @Query("SELECT DISTINCT c FROM Customer c "
            + "JOIN c.contacts ct "
            + "WHERE ct.email = :email")
    Customer findByContactEmail(
            @Param("email") String email);
}
