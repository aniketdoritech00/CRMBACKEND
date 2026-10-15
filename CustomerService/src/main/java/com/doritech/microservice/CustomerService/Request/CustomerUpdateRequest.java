package com.doritech.microservice.CustomerService.Request;

import jakarta.validation.constraints.Size;

public class CustomerUpdateRequest {

    @Size(max = 255, message = "Customer name must not exceed 255 characters")
    private String name;

    @Size(max = 255, message = "Legal name must not exceed 255 characters")
    private String legalName;

    private String status;

    private Long controllingOfficeId;

    @Size(max = 100, message = "Industry must not exceed 100 characters")
    private String industry;

    @Size(max = 255, message = "Website must not exceed 255 characters")
    private String website;

    @Size(max = 15, message = "GST number must not exceed 15 characters")
    private String gstNumber;

    @Size(max = 10, message = "PAN number must not exceed 10 characters")
    private String panNumber;

    private Double annualBudget;

    private String preferredCommunicationMode;

    private String remarks;

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

	public String getGstNumber() {
		return gstNumber;
	}

	public void setGstNumber(String gstNumber) {
		this.gstNumber = gstNumber;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	public Double getAnnualBudget() {
		return annualBudget;
	}

	public void setAnnualBudget(Double annualBudget) {
		this.annualBudget = annualBudget;
	}

	public String getPreferredCommunicationMode() {
		return preferredCommunicationMode;
	}

	public void setPreferredCommunicationMode(String preferredCommunicationMode) {
		this.preferredCommunicationMode = preferredCommunicationMode;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}



}

//package com.doritech.microservice.CustomerService.Request;
//
//import com.doritech.microservice.CustomerService.Entity.Customer;
//
//import jakarta.validation.constraints.Size;
//
//
//
//public class CustomerUpdateRequest {
//    
//    @Size(max = 255, message = "Customer name must not exceed 255 characters")
//    private String name;
//    
//    @Size(max = 255, message = "Legal name must not exceed 255 characters")
//    private String legalName;
//    
//    private Customer.CustomerStatus status;
//    
//    private Long controllingOfficeId;
//    
//    @Size(max = 100, message = "Industry must not exceed 100 characters")
//    private String industry;
//    
//    @Size(max = 255, message = "Website must not exceed 255 characters")
//    private String website;
//    
//    @Size(max = 15, message = "GST number must not exceed 15 characters")
//    private String gstNumber;
//    
//    @Size(max = 10, message = "PAN number must not exceed 10 characters")
//    private String panNumber;
//
//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	public String getLegalName() {
//		return legalName;
//	}
//
//	public void setLegalName(String legalName) {
//		this.legalName = legalName;
//	}
//
//	public Customer.CustomerStatus getStatus() {
//		return status;
//	}
//
//	public void setStatus(Customer.CustomerStatus status) {
//		this.status = status;
//	}
//
//	public Long getControllingOfficeId() {
//		return controllingOfficeId;
//	}
//
//	public void setControllingOfficeId(Long controllingOfficeId) {
//		this.controllingOfficeId = controllingOfficeId;
//	}
//
//	public String getIndustry() {
//		return industry;
//	}
//
//	public void setIndustry(String industry) {
//		this.industry = industry;
//	}
//
//	public String getWebsite() {
//		return website;
//	}
//
//	public void setWebsite(String website) {
//		this.website = website;
//	}
//
//	public String getGstNumber() {
//		return gstNumber;
//	}
//
//	public void setGstNumber(String gstNumber) {
//		this.gstNumber = gstNumber;
//	}
//
//	public String getPanNumber() {
//		return panNumber;
//	}
//
//	public void setPanNumber(String panNumber) {
//		this.panNumber = panNumber;
//	}
//    
//    
//}