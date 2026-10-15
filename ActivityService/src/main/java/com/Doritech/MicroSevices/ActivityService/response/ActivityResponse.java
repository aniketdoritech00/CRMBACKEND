package com.Doritech.MicroSevices.ActivityService.response;

import java.time.LocalDateTime;

import com.Doritech.MicroSevices.ActivityService.entity.Activity.ActivityStatus;
import com.Doritech.MicroSevices.ActivityService.entity.Activity.ActivityType;

public class ActivityResponse {

    private Integer id;
    private Integer customerId;
    private Integer contactId;
    private ActivityType type;
    private String subject;
    private String description;
    private ActivityStatus status;
    private LocalDateTime scheduledAt;
    private LocalDateTime performedAt;
    private String createdBy;
    private LocalDateTime createdAt;

    public ActivityResponse() {}

    public ActivityResponse(Integer id, Integer customerId, Integer contactId, ActivityType type, String subject, String description, ActivityStatus status, LocalDateTime scheduledAt, LocalDateTime performedAt, String createdBy, LocalDateTime createdAt) {
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
}
