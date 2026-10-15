package com.doritech.competitor_api.request;

import jakarta.validation.constraints.Size;

public class UpdateCompetitorRequest {

	@Size(max = 255, message = "Name must not exceed 255 characters")
	private String name;

	@Size(max = 1000, message = "Description must not exceed 1000 characters")
	private String description;

	@Size(max = 255, message = "Website must not exceed 255 characters")
	private String website;

	@Size(max = 100, message = "Industry must not exceed 100 characters")
	private String industry;

	public UpdateCompetitorRequest() {
	}

	public UpdateCompetitorRequest(String name, String description, String website, String industry) {
		this.name = name;
		this.description = description;
		this.website = website;
		this.industry = industry;
	}

	// Getters and Setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}
}