package com.Doritech.MicroSevices.ActivityService.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.Doritech.MicroSevices.ActivityService.entity.Activity.ActivityStatus;
import com.Doritech.MicroSevices.ActivityService.entity.Activity.ActivityType;

public class ActivityRequest {

	@NotNull(message = "Customer ID is required")
	private Integer customerId;

	private Integer contactId;

	@NotNull(message = "Activity type is required")
	private ActivityType type;

	@NotBlank(message = "Subject cannot be blank")
	private String subject;
	

	private String description;

	private ActivityStatus status;

	private LocalDateTime scheduledAt;

	private LocalDateTime performedAt;

	private String createdBy;

	public ActivityRequest() {
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getContactId() {
		return contactId;
	}

	public void setContactId(Integer contactId) {
		this.contactId = contactId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ActivityStatus getStatus() {
		return status;
	}

	public void setStatus(ActivityStatus status) {
		this.status = status;
	}

	public LocalDateTime getScheduledAt() {
		return scheduledAt;
	}

	public void setScheduledAt(LocalDateTime scheduledAt) {
		this.scheduledAt = scheduledAt;
	}

	public LocalDateTime getPerformedAt() {
		return performedAt;
	}

	public void setPerformedAt(LocalDateTime performedAt) {
		this.performedAt = performedAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public ActivityType getType() {
		return type;
	}

	public void setType(ActivityType type) {
		this.type = type;
	}
}
