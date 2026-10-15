package com.doritech.microservice.CustomerService.Request;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.doritech.microservice.CustomerService.Enum.CommunicationMode;
import com.doritech.microservice.CustomerService.Enum.ContactType;
import com.doritech.microservice.CustomerService.Enum.CustomerStatus;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CustomerRequest {

    @NotBlank(message = "Customer name is required")
    @Size(max = 100, message = "Customer name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "Legal name is required")
    @Size(max = 100, message = "Legal name cannot exceed 100 characters")
    private String legalName;

    private CustomerStatus status = CustomerStatus.ACTIVE;

    // Existing field from the second version
    private Long controllingOfficeId;

    @Size(max = 100, message = "Industry cannot exceed 100 characters")
    private String industry;

    @Pattern(
        regexp = "^(https?://)?([\\w-])+\\.[\\w-]+.*$",
        message = "Invalid website URL"
    )
    private String website;

    @Pattern(
        regexp = "^[0-9A-Z]{15}$",
        message = "Invalid GST number"
    )
    private String gstNumber;

    private String serviceUsed;
    private String annualBudget;
    private ContactType contactType;

    @FutureOrPresent(message = "Contract start date must be today or future")
    private Date contractStartDate;

    @Future(message = "Contract end date must be in the future")
    private Date contractEndDate;

    private CommunicationMode preferredCommunicationMode;

    @Size(max = 500, message = "Remarks cannot exceed 500 characters")
    private String remarks;

    @NotEmpty(message = "At least one address is required")
    private Set<@Valid CustomerAddressRequest> addresses;

    @NotEmpty(message = "At least one contact is required")
    private Set<@Valid ContactRequest> contacts;

    // Tag IDs required in the request
    private List<Long> tagIds;

    // Additional fields from the first version
    private String officeBranch;
    private String organization;
    private String branch;
    private String siteType;
    private String hierarchy;
    private String ifsc;
    private String zoneName;
    private String faName;

    // Default constructor
    public CustomerRequest() {
        this.status = CustomerStatus.ACTIVE;
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

    // GST number
    public String getGstNumber() {
        return gstNumber;
    }

    public void setGstNumber(String gstNumber) {
        this.gstNumber = gstNumber;
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

    public void setPreferredCommunicationMode(
            CommunicationMode preferredCommunicationMode) {
        this.preferredCommunicationMode = preferredCommunicationMode;
    }

    // Remarks
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    // Addresses
    public Set<CustomerAddressRequest> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<CustomerAddressRequest> addresses) {
        this.addresses = addresses;
    }

    // Contacts
    public Set<ContactRequest> getContacts() {
        return contacts;
    }

    public void setContacts(Set<ContactRequest> contacts) {
        this.contacts = contacts;
    }

    // Tag IDs
    public List<Long> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Long> tagIds) {
        this.tagIds = tagIds;
    }

    // Office branch
    public String getOfficeBranch() {
        return officeBranch;
    }

    public void setOfficeBranch(String officeBranch) {
        this.officeBranch = officeBranch;
    }

    // Organization
    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    // Branch
    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
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
}
