package com.Doritech.MicroSevices.ActivityService.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Doritech.MicroSevices.ActivityService.entity.Activity.ActivityStatus;
import com.Doritech.MicroSevices.ActivityService.entity.Activity.ActivityType;
import com.Doritech.MicroSevices.ActivityService.request.ActivityRequest;
import com.Doritech.MicroSevices.ActivityService.response.ActivityResponse;
import com.Doritech.MicroSevices.ActivityService.service.ActivityService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/activities")
@CrossOrigin(allowedHeaders = "*")
public class ActivityController {
	@Autowired
	private ActivityService activityService;

	@PostMapping
	public ResponseEntity<ActivityResponse> createActivity(@Valid @RequestBody ActivityRequest request) {
		ActivityResponse response = activityService.createActivity(request);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ActivityResponse> getActivityById(@PathVariable Integer id) {
		ActivityResponse response = activityService.getActivityById(id);
		return ResponseEntity.ok(response);
	}

	@GetMapping
	public ResponseEntity<List<ActivityResponse>> getAllActivities() {
		List<ActivityResponse> responses = activityService.getAllActivities();
		return ResponseEntity.ok(responses);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ActivityResponse> updateActivity(@PathVariable Integer id,
			@Valid @RequestBody ActivityRequest request) {
		ActivityResponse response = activityService.updateActivity(id, request);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteActivity(@PathVariable Integer id) {
		activityService.deleteActivity(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/customer/{customerId}")
	public ResponseEntity<List<ActivityResponse>> getActivitiesByCustomerId(@PathVariable Integer customerId) {
		List<ActivityResponse> responses = activityService.getActivitiesByCustomerId(customerId);
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/contact/{contactId}")
	public ResponseEntity<List<ActivityResponse>> getActivitiesByContactId(@PathVariable Integer contactId) {
		List<ActivityResponse> responses = activityService.getActivitiesByContactId(contactId);
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/status/{status}")
	public ResponseEntity<List<ActivityResponse>> getActivitiesByStatus(@PathVariable ActivityStatus status) {
		List<ActivityResponse> responses = activityService.getActivitiesByStatus(status);
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/type/{type}")
	public ResponseEntity<List<ActivityResponse>> getActivitiesByType(@PathVariable ActivityType type) {
		List<ActivityResponse> responses = activityService.getActivitiesByType(type);
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/created-by/{createdBy}")
	public ResponseEntity<List<ActivityResponse>> getActivitiesByCreatedBy(@PathVariable String createdBy) {
		List<ActivityResponse> responses = activityService.getActivitiesByCreatedBy(createdBy);
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/customer/{customerId}/status/{status}")
	public ResponseEntity<List<ActivityResponse>> getActivitiesByCustomerIdAndStatus(@PathVariable Integer customerId,
			@PathVariable ActivityStatus status) {
		List<ActivityResponse> responses = activityService.getActivitiesByCustomerIdAndStatus(customerId, status);
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/scheduled")
	public ResponseEntity<List<ActivityResponse>> getActivitiesScheduledBetween(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
		List<ActivityResponse> responses = activityService.getActivitiesScheduledBetween(startDate, endDate);
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/upcoming")
	public ResponseEntity<List<ActivityResponse>> getUpcomingScheduledActivities() {
		List<ActivityResponse> responses = activityService.getUpcomingScheduledActivities();
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/overdue")
	public ResponseEntity<List<ActivityResponse>> getOverdueActivities() {
		List<ActivityResponse> responses = activityService.getOverdueActivities();
		return ResponseEntity.ok(responses);
	}

	@PatchMapping("/{id}/status")
	public ResponseEntity<ActivityResponse> updateActivityStatus(@PathVariable Integer id,
			@RequestParam ActivityStatus status) {
		ActivityResponse response = activityService.updateActivityStatus(id, status);
		return ResponseEntity.ok(response);
	}

	@PatchMapping("/{id}/complete")
	public ResponseEntity<ActivityResponse> markActivityAsCompleted(@PathVariable Integer id) {
		ActivityResponse response = activityService.markActivityAsCompleted(id);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/customer/{customerId}/count")
	public ResponseEntity<Long> countActivitiesByCustomerId(@PathVariable Integer customerId) {
		long count = activityService.countActivitiesByCustomerId(customerId);
		return ResponseEntity.ok(count);
	}
}
