package com.Doritech.MicroSevices.ActivityService.service;

import java.time.LocalDateTime;
import java.util.List;

import com.Doritech.MicroSevices.ActivityService.entity.Activity.ActivityStatus;
import com.Doritech.MicroSevices.ActivityService.entity.Activity.ActivityType;
import com.Doritech.MicroSevices.ActivityService.request.ActivityRequest;
import com.Doritech.MicroSevices.ActivityService.response.ActivityResponse;

public interface ActivityService {

    ActivityResponse createActivity(ActivityRequest request);

    ActivityResponse getActivityById(Integer id);

    List<ActivityResponse> getAllActivities();

    ActivityResponse updateActivity(Integer id, ActivityRequest request);

    void deleteActivity(Integer id);

    List<ActivityResponse> getActivitiesByCustomerId(Integer customerId);

    List<ActivityResponse> getActivitiesByContactId(Integer contactId);

    List<ActivityResponse> getActivitiesByStatus(ActivityStatus status);

    List<ActivityResponse> getActivitiesByType(ActivityType type);

    List<ActivityResponse> getActivitiesByCreatedBy(String createdBy);

    List<ActivityResponse> getActivitiesByCustomerIdAndStatus(Integer customerId, ActivityStatus status);

    List<ActivityResponse> getActivitiesScheduledBetween(LocalDateTime start, LocalDateTime end);

    List<ActivityResponse> getUpcomingScheduledActivities();

    List<ActivityResponse> getOverdueActivities();

    ActivityResponse updateActivityStatus(Integer id, ActivityStatus status);

    ActivityResponse markActivityAsCompleted(Integer id);

    long countActivitiesByCustomerId(Integer customerId);
}
