package com.doritech.competitor_api.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.doritech.competitor_api.request.CreateCompetitorRequest;
import com.doritech.competitor_api.request.UpdateCompetitorRequest;
import com.doritech.competitor_api.response.CompetitorListResponse;
import com.doritech.competitor_api.response.CompetitorResponse;

public interface CompetitorService {

	CompetitorResponse createCompetitor(CreateCompetitorRequest request);

	CompetitorResponse getCompetitorById(Long id);

	CompetitorListResponse getAllCompetitors(Pageable pageable);

	CompetitorResponse updateCompetitor(Long id, UpdateCompetitorRequest request);

	void deleteCompetitor(Long id);

	CompetitorListResponse searchCompetitors(String searchTerm, Pageable pageable);

	CompetitorListResponse getCompetitorsByIndustry(String industry, Pageable pageable);

	List<String> getAllIndustries();

	boolean existsByName(String name);

	void uploadCompetitorsFromExcel(MultipartFile file) throws Exception;
}