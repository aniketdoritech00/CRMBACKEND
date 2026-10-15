package com.Doritech.MicroSevices.ActivityService.seviceImp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Doritech.MicroSevices.ActivityService.entity.Activity;
import com.Doritech.MicroSevices.ActivityService.entity.Activity.ActivityStatus;
import com.Doritech.MicroSevices.ActivityService.entity.Activity.ActivityType;
import com.Doritech.MicroSevices.ActivityService.exception.GlobalException;
import com.Doritech.MicroSevices.ActivityService.mapper.ActivityMapper;
import com.Doritech.MicroSevices.ActivityService.repository.ActivityRepository;
import com.Doritech.MicroSevices.ActivityService.request.ActivityRequest;
import com.Doritech.MicroSevices.ActivityService.response.ActivityResponse;
import com.Doritech.MicroSevices.ActivityService.service.ActivityService;

@Service
public class ActivityServiceImpl implements ActivityService {

	private final ActivityRepository repository;
	private final ActivityMapper mapper;

	@Autowired
	public ActivityServiceImpl(ActivityRepository repository, ActivityMapper mapper) {
		this.repository = repository;
		this.mapper = mapper;
	}

	@Override
	public ActivityResponse createActivity(ActivityRequest request) {
		Activity entity = mapper.toEntity(request);
		Activity saved = repository.save(entity);
		return mapper.toResponse(saved);
	}

	@Override
	public ActivityResponse getActivityById(Integer id) {
		Activity activity = repository.findById(id)
				.orElseThrow(() -> new GlobalException("Activity not found with id: " + id));
		return mapper.toResponse(activity);
	}

	@Override
	public List<ActivityResponse> getAllActivities() {
		return repository.findAll().stream().map(mapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public ActivityResponse updateActivity(Integer id, ActivityRequest request) {
		Activity existing = repository.findById(id)
				.orElseThrow(() -> new GlobalException("Activity not found with id: " + id));
		if (request.getCustomerId() != null)
			existing.setCustomerId(request.getCustomerId());
		if (request.getContactId() != null)
			existing.setContactId(request.getContactId());
		if (request.getType() != null)
			existing.setType(request.getType());
		if (request.getSubject() != null)
			existing.setSubject(request.getSubject());
		if (request.getDescription() != null)
			existing.setDescription(request.getDescription());
		if (request.getStatus() != null)
			existing.setStatus(request.getStatus());
		if (request.getScheduledAt() != null)
			existing.setScheduledAt(request.getScheduledAt());
		if (request.getPerformedAt() != null)
			existing.setPerformedAt(request.getPerformedAt());
		if (request.getCreatedBy() != null)
			existing.setCreatedBy(request.getCreatedBy());
		Activity saved = repository.save(existing);
		return mapper.toResponse(saved);
	}

	@Override
	public void deleteActivity(Integer id) {
		Activity existing = repository.findById(id)
				.orElseThrow(() -> new GlobalException("Activity not found with id: " + id));
		repository.delete(existing);
	}

	@Override
	public List<ActivityResponse> getActivitiesByCustomerId(Integer customerId) {
		return repository.findByCustomerId(customerId).stream().map(mapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<ActivityResponse> getActivitiesByContactId(Integer contactId) {
		return repository.findByContactId(contactId).stream().map(mapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<ActivityResponse> getActivitiesByStatus(ActivityStatus status) {
		return repository.findByStatus(status).stream().map(mapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<ActivityResponse> getActivitiesByType(ActivityType type) {
		return repository.findByType(type).stream().map(mapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<ActivityResponse> getActivitiesByCreatedBy(String createdBy) {
		return repository.findByCreatedBy(createdBy).stream().map(mapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<ActivityResponse> getActivitiesByCustomerIdAndStatus(Integer customerId, ActivityStatus status) {
		return repository.findByCustomerIdAndStatus(customerId, status).stream().map(mapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<ActivityResponse> getActivitiesScheduledBetween(LocalDateTime start, LocalDateTime end) {
		return repository.findByScheduledAtBetween(start, end).stream().map(mapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<ActivityResponse> getUpcomingScheduledActivities() {
		LocalDateTime now = LocalDateTime.now();
		return repository.findByScheduledAtAfterAndStatus(now, ActivityStatus.scheduled).stream()
				.map(mapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<ActivityResponse> getOverdueActivities() {
		LocalDateTime now = LocalDateTime.now();
		return repository.findByScheduledAtBeforeAndStatus(now, ActivityStatus.scheduled).stream()
				.map(mapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public ActivityResponse updateActivityStatus(Integer id, ActivityStatus status) {
		Activity existing = repository.findById(id)
				.orElseThrow(() -> new GlobalException("Activity not found with id: " + id));
		existing.setStatus(status);
		if (status == ActivityStatus.completed && existing.getPerformedAt() == null) {
			existing.setPerformedAt(LocalDateTime.now());
		}
		Activity saved = repository.save(existing);
		return mapper.toResponse(saved);
	}

	@Override
	public ActivityResponse markActivityAsCompleted(Integer id) {
		Activity existing = repository.findById(id)
				.orElseThrow(() -> new GlobalException("Activity not found with id: " + id));
		existing.setStatus(ActivityStatus.completed);
		existing.setPerformedAt(LocalDateTime.now());
		Activity saved = repository.save(existing);
		return mapper.toResponse(saved);
	}

	@Override
	public long countActivitiesByCustomerId(Integer customerId) {
		return repository.countByCustomerId(customerId);
	}
}
