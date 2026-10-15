package com.Doritech.MicroSevices.ActivityService.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "activities")
public class Activity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "customer_id", nullable = false)
	private Integer customerId;

	@Column(name = "contact_id")
	private Integer contactId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ActivityType type;

	@Column(nullable = false, length = 255)
	private String subject;

	@Lob
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ActivityStatus status = ActivityStatus.scheduled;

	@Column(name = "scheduled_at")
	private LocalDateTime scheduledAt;

	@Column(name = "performed_at")
	private LocalDateTime performedAt;

	@Column(name = "created_by", length = 100)
	private String createdBy;

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	public Activity() {
	}

	public Activity(Integer id, Integer customerId, Integer contactId, ActivityType type, String subject,
			String description, ActivityStatus status, LocalDateTime scheduledAt, LocalDateTime performedAt,
			String createdBy, LocalDateTime createdAt) {
		this.id = id;
		this.customerId = customerId;
		this.contactId = contactId;
		this.type = type;
		this.subject = subject;
		this.description = description;
		this.status = status;
		this.scheduledAt = scheduledAt;
		this.performedAt = performedAt;
		this.createdBy = createdBy;
		this.createdAt = createdAt;
	}

	@PrePersist
	protected void onCreate() {
		if (this.createdAt == null) {
			this.createdAt = LocalDateTime.now();
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public ActivityType getType() {
		return type;
	}

	public void setType(ActivityType type) {
		this.type = type;
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

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public enum ActivityType {
		call, email, meeting, demo, follow_up, proposal
	}

	public enum ActivityStatus {
		scheduled, completed, cancelled, postponed
	}
}
