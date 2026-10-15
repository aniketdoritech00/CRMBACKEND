package com.doritech.microservice.CustomerService.Response;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.doritech.microservice.CustomerService.Enum.CommunicationMode;
import com.doritech.microservice.CustomerService.Enum.ContactType;

public class CustomerResponse {

	private Long id;
	private String customerCode;
	private String name;
	private String legalName;

	// Use String because your mapper converts status to its value
	private String status;

	private Long controllingOfficeId;
	private String industry;
	private String website;

	// Additional fields from Customer entity
	private String branch;
	private String officeBranch;
	private String siteType;
	private String hierarchy;
	private String gstNumber;
	private String organization;
	private String bankName;
	private String ifsc;
	private String zoneName;
	private String faName;

	private String serviceUsed;
	private String annualBudget;
	private ContactType contactType;
	private Date contractStartDate;
	private Date contractEndDate;
	private CommunicationMode preferredCommunicationMode;
	private String remarks;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	private Set<CustomerAddressResponse> addresses;
	private Set<ContactResponse> contacts;
	private List<TagResponse> tags;

	public CustomerResponse() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLegalName() {
		return legalName;
	}

	public void setLegalName(String legalName) {
		this.legalName = legalName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getControllingOfficeId() {
		return controllingOfficeId;
	}

	public void setControllingOfficeId(Long controllingOfficeId) {
		this.controllingOfficeId = controllingOfficeId;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getOfficeBranch() {
		return officeBranch;
	}

	public void setOfficeBranch(String officeBranch) {
		this.officeBranch = officeBranch;
	}

	public String getSiteType() {
		return siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}

	public String getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(String hierarchy) {
		this.hierarchy = hierarchy;
	}

	public String getGstNumber() {
		return gstNumber;
	}

	public void setGstNumber(String gstNumber) {
		this.gstNumber = gstNumber;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getIfsc() {
		return ifsc;
	}

	public void setIfsc(String ifsc) {
		this.ifsc = ifsc;
	}

	public String getZoneName() {
		return zoneName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	public String getFaName() {
		return faName;
	}

	public void setFaName(String faName) {
		this.faName = faName;
	}

	public String getServiceUsed() {
		return serviceUsed;
	}

	public void setServiceUsed(String serviceUsed) {
		this.serviceUsed = serviceUsed;
	}

	public String getAnnualBudget() {
		return annualBudget;
	}

	public void setAnnualBudget(String annualBudget) {
		this.annualBudget = annualBudget;
	}

	public ContactType getContactType() {
		return contactType;
	}

	public void setContactType(ContactType contactType) {
		this.contactType = contactType;
	}

	public Date getContractStartDate() {
		return contractStartDate;
	}

	public void setContractStartDate(Date contractStartDate) {
		this.contractStartDate = contractStartDate;
	}

	public Date getContractEndDate() {
		return contractEndDate;
	}

	public void setContractEndDate(Date contractEndDate) {
		this.contractEndDate = contractEndDate;
	}

	public CommunicationMode getPreferredCommunicationMode() {
		return preferredCommunicationMode;
	}

	public void setPreferredCommunicationMode(CommunicationMode preferredCommunicationMode) {
		this.preferredCommunicationMode = preferredCommunicationMode;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Set<CustomerAddressResponse> getAddresses() {
		return addresses;
	}

	public void setAddresses(Set<CustomerAddressResponse> addresses) {
		this.addresses = addresses;
	}

	public Set<ContactResponse> getContacts() {
		return contacts;
	}

	public void setContacts(Set<ContactResponse> contacts) {
		this.contacts = contacts;
	}

	public List<TagResponse> getTags() {
		return tags;
	}

	public void setTags(List<TagResponse> tags) {
		this.tags = tags;
	}
}