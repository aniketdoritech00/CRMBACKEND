package com.Doritech.MicroSevices.ActivityService.mapper;


import org.mapstruct.Mapper;

import com.Doritech.MicroSevices.ActivityService.entity.Activity;
import com.Doritech.MicroSevices.ActivityService.request.ActivityRequest;
import com.Doritech.MicroSevices.ActivityService.response.ActivityResponse;

@Mapper(componentModel = "spring")
public interface ActivityMapper {

    Activity toEntity(ActivityRequest request);

    ActivityResponse toResponse(Activity activity);
}
