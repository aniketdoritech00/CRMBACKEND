package com.doritech.competitor_api.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.doritech.competitor_api.model.Competitor;
import com.doritech.competitor_api.request.CreateCompetitorRequest;
import com.doritech.competitor_api.request.UpdateCompetitorRequest;
import com.doritech.competitor_api.response.CompetitorResponse;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CompetitorMapper {

    Competitor toEntity(CreateCompetitorRequest request);

    CompetitorResponse toResponse(Competitor competitor);

    List<CompetitorResponse> toResponseList(List<Competitor> competitors);

    void updateEntityFromRequest(UpdateCompetitorRequest request, @MappingTarget Competitor competitor);
}
