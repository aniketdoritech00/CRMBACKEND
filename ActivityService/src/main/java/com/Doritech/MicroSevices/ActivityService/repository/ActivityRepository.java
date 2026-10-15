package com.Doritech.MicroSevices.ActivityService.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Doritech.MicroSevices.ActivityService.entity.Activity;
import com.Doritech.MicroSevices.ActivityService.entity.Activity.ActivityStatus;
import com.Doritech.MicroSevices.ActivityService.entity.Activity.ActivityType;

public interface ActivityRepository extends JpaRepository<Activity, Integer> {

    List<Activity> findByCustomerId(Integer customerId);

    List<Activity> findByContactId(Integer contactId);

    List<Activity> findByStatus(ActivityStatus status);

    List<Activity> findByType(ActivityType type);

    List<Activity> findByCreatedBy(String createdBy);

    List<Activity> findByCustomerIdAndStatus(Integer customerId, ActivityStatus status);

    List<Activity> findByScheduledAtBetween(LocalDateTime start, LocalDateTime end);

    List<Activity> findByScheduledAtAfterAndStatus(LocalDateTime start, ActivityStatus status);

    List<Activity> findByScheduledAtBeforeAndStatus(LocalDateTime end, ActivityStatus status);

    long countByCustomerId(Integer customerId);
}
