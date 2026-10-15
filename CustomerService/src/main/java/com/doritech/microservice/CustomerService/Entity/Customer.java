package com.doritech.microservice.CustomerService.Entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.doritech.microservice.CustomerService.Enum.CommunicationMode;
import com.doritech.microservice.CustomerService.Enum.ContactType;
import com.doritech.microservice.CustomerService.Enum.CustomerStatus;
import com.doritech.microservice.CustomerService.Request.ContactTypeConverter;
import com.doritech.microservice.CustomerService.Request.CustomerStatusConverter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Customer")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "customer_code", unique = true, nullable = false)
	private String customerCode;

	@Column(nullable = false)
	private String name;

	@Column(name = "legal_name")
	private String legalName;

	@Convert(converter = CustomerStatusConverter.class)
	@Column(name = "status", columnDefinition = "ENUM('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE'")
	private CustomerStatus status;

	// Retained from the second version
	@Column(name = "controlling_office_id")
	private Long controllingOfficeId;

	private String industry;
	private String website;

	// Additional fields from the first version
	private String branch;

	@Column(name = "office_branch")
	private String officeBranch;

	@Column(name = "site_type")
	private String siteType;

	private String hierarchy;

	@Column(name = "gst_number")
	private String gstNumber;

	@Column(name = "organization")
	private String organization;

	@Column(name = "bank_name")
	private String bankName;

	@Column(name = "ifsc_code")
	private String ifsc;

	@Column(name = "zone_name")
	private String zoneName;

	@Column(name = "fa_name")
	private String faName;

	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<CustomerAddress> addresses = new HashSet<>();

	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Contact> contacts = new HashSet<>();

	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<CustomerTag> customerTags;

	@Column(name = "services_used")
	private String serviceUsed;

	@Column(name = "annual_budget")
	private String annualBudget;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Convert(converter = ContactTypeConverter.class)
	@Column(name = "contact_type", columnDefinition = "ENUM('Direct AMC', 'Direct Installation','Indirect-Contract','Ex-Client')")
	private ContactType contactType;

	// Retained from the second version
	@Column(name = "contract_start_date")
	private Date contractStartDate;

	@Column(name = "contract_end_date")
	private Date contractEndDate;

	@Column(name = "preferred_communication_mode")
	@Enumerated(EnumType.STRING)
	private CommunicationMode preferredCommunicationMode;

	@Column(name = "remarks")
	private String remarks;

	// Default constructor
	public Customer() {
	}

	// ID
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	// Customer code
	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	// Name
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// Legal name
	public String getLegalName() {
		return legalName;
	}

	public void setLegalName(String legalName) {
		this.legalName = legalName;
	}

	// Status
	public CustomerStatus getStatus() {
		return status;
	}

	public void setStatus(CustomerStatus status) {
		this.status = status;
	}

	// Controlling office ID
	public Long getControllingOfficeId() {
		return controllingOfficeId;
	}

	public void setControllingOfficeId(Long controllingOfficeId) {
		this.controllingOfficeId = controllingOfficeId;
	}

	// Industry
	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	// Website
	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	// Branch
	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	// Office branch
	public String getOfficeBranch() {
		return officeBranch;
	}

	public void setOfficeBranch(String officeBranch) {
		this.officeBranch = officeBranch;
	}

	// Site type
	public String getSiteType() {
		return siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}

	// Hierarchy
	public String getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(String hierarchy) {
		this.hierarchy = hierarchy;
	}

	// GST number
	public String getGstNumber() {
		return gstNumber;
	}

	public void setGstNumber(String gstNumber) {
		this.gstNumber = gstNumber;
	}

	// Organization
	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	// Bank name
	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	// IFSC
	public String getIfsc() {
		return ifsc;
	}

	public void setIfsc(String ifsc) {
		this.ifsc = ifsc;
	}

	// Zone name
	public String getZoneName() {
		return zoneName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	// FA name
	public String getFaName() {
		return faName;
	}

	public void setFaName(String faName) {
		this.faName = faName;
	}

	// Addresses
	public Set<CustomerAddress> getAddresses() {
		return addresses;
	}

	public void setAddresses(Set<CustomerAddress> addresses) {
		this.addresses = addresses;
	}

	// Contacts
	public Set<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(Set<Contact> contacts) {
		this.contacts = contacts;
	}

	// Customer tags
	public List<CustomerTag> getCustomerTags() {
		return customerTags;
	}

	public void setCustomerTags(List<CustomerTag> customerTags) {
		this.customerTags = customerTags;
	}

	// Service used
	public String getServiceUsed() {
		return serviceUsed;
	}

	public void setServiceUsed(String serviceUsed) {
		this.serviceUsed = serviceUsed;
	}

	// Annual budget
	public String getAnnualBudget() {
		return annualBudget;
	}

	public void setAnnualBudget(String annualBudget) {
		this.annualBudget = annualBudget;
	}

	// Created at
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	// Updated at
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	// Contact type
	public ContactType getContactType() {
		return contactType;
	}

	public void setContactType(ContactType contactType) {
		this.contactType = contactType;
	}

	// Contract start date
	public Date getContractStartDate() {
		return contractStartDate;
	}

	public void setContractStartDate(Date contractStartDate) {
		this.contractStartDate = contractStartDate;
	}

	// Contract end date
	public Date getContractEndDate() {
		return contractEndDate;
	}

	public void setContractEndDate(Date contractEndDate) {
		this.contractEndDate = contractEndDate;
	}

	// Preferred communication mode
	public CommunicationMode getPreferredCommunicationMode() {
		return preferredCommunicationMode;
	}

	public void setPreferredCommunicationMode(CommunicationMode preferredCommunicationMode) {
		this.preferredCommunicationMode = preferredCommunicationMode;
	}

	// Remarks
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Deprecated
	public String getRemakrs() {
		return remarks;
	}

	@Deprecated
	public void setRemakrs(String remakrs) {
		this.remarks = remakrs;
	}
}
