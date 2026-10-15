package com.doritech.competitor_api.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.doritech.competitor_api.exception.CompetitorAlreadyExistsException;
import com.doritech.competitor_api.exception.CompetitorNotFoundException;
import com.doritech.competitor_api.mapper.CompetitorMapper;
import com.doritech.competitor_api.model.Competitor;
import com.doritech.competitor_api.repository.CompetitorRepository;
import com.doritech.competitor_api.request.CreateCompetitorRequest;
import com.doritech.competitor_api.request.UpdateCompetitorRequest;
import com.doritech.competitor_api.response.CompetitorListResponse;
import com.doritech.competitor_api.response.CompetitorResponse;
import com.doritech.competitor_api.service.CompetitorService;

@Service
@Transactional
public class CompetitorServiceImpl implements CompetitorService {

	private static final Logger logger = LoggerFactory.getLogger(CompetitorServiceImpl.class);

	private final CompetitorRepository competitorRepository;
	private final CompetitorMapper competitorMapper;

	@Autowired
	public CompetitorServiceImpl(CompetitorRepository competitorRepository, CompetitorMapper competitorMapper) {
		this.competitorRepository = competitorRepository;
		this.competitorMapper = competitorMapper;
	}

	@Override
	public CompetitorResponse createCompetitor(CreateCompetitorRequest request) {
		logger.info("Creating new competitor with name: {}", request.getName());

		if (competitorRepository.existsByNameIgnoreCase(request.getName())) {
			throw new CompetitorAlreadyExistsException(
					"Competitor with name '" + request.getName() + "' already exists");
		}

		Competitor competitor = competitorMapper.toEntity(request);
		Competitor savedCompetitor = competitorRepository.save(competitor);

		logger.info("Successfully created competitor with ID: {}", savedCompetitor.getId());
		return competitorMapper.toResponse(savedCompetitor);
	}

	@Override
	@Transactional(readOnly = true)
	public CompetitorResponse getCompetitorById(Long id) {
		logger.info("Fetching competitor with ID: {}", id);

		Competitor competitor = competitorRepository.findById(id)
				.orElseThrow(() -> new CompetitorNotFoundException("Competitor not found with ID: " + id));

		return competitorMapper.toResponse(competitor);
	}

	@Override
	@Transactional(readOnly = true)
	public CompetitorListResponse getAllCompetitors(Pageable pageable) {
		logger.info("Fetching all competitors with pagination: page={}, size={}", pageable.getPageNumber(),
				pageable.getPageSize());

		Page<Competitor> competitorPage = competitorRepository.findAll(pageable);
		List<CompetitorResponse> competitorResponses = competitorMapper.toResponseList(competitorPage.getContent());

		return new CompetitorListResponse(competitorResponses, (int) competitorPage.getTotalElements(),
				competitorPage.getTotalPages(), competitorPage.getNumber(), competitorPage.getSize());
	}

	@Override
	public CompetitorResponse updateCompetitor(Long id, UpdateCompetitorRequest request) {
		logger.info("Updating competitor with ID: {}", id);

		Competitor existingCompetitor = competitorRepository.findById(id)
				.orElseThrow(() -> new CompetitorNotFoundException("Competitor not found with ID: " + id));

		if (StringUtils.hasText(request.getName()) && !request.getName().equalsIgnoreCase(existingCompetitor.getName())
				&& competitorRepository.existsByNameIgnoreCase(request.getName())) {
			throw new CompetitorAlreadyExistsException(
					"Competitor with name '" + request.getName() + "' already exists");
		}

		competitorMapper.updateEntityFromRequest(request, existingCompetitor);
		Competitor updatedCompetitor = competitorRepository.save(existingCompetitor);

		logger.info("Successfully updated competitor with ID: {}", id);
		return competitorMapper.toResponse(updatedCompetitor);
	}

	@Override
	public void deleteCompetitor(Long id) {
		logger.info("Deleting competitor with ID: {}", id);

		if (!competitorRepository.existsById(id)) {
			throw new CompetitorNotFoundException("Competitor not found with ID: " + id);
		}

		competitorRepository.deleteById(id);
		logger.info("Successfully deleted competitor with ID: {}", id);
	}

	@Override
	@Transactional(readOnly = true)
	public CompetitorListResponse searchCompetitors(String searchTerm, Pageable pageable) {
		logger.info("Searching competitors with term: {} and pagination: page={}, size={}", searchTerm,
				pageable.getPageNumber(), pageable.getPageSize());

		Page<Competitor> competitorPage = competitorRepository.searchCompetitors(searchTerm, pageable);
		List<CompetitorResponse> competitorResponses = competitorMapper.toResponseList(competitorPage.getContent());

		return new CompetitorListResponse(competitorResponses, (int) competitorPage.getTotalElements(),
				competitorPage.getTotalPages(), competitorPage.getNumber(), competitorPage.getSize());
	}

	@Override
	@Transactional(readOnly = true)
	public CompetitorListResponse getCompetitorsByIndustry(String industry, Pageable pageable) {
		logger.info("Fetching competitors by industry: {} with pagination: page={}, size={}", industry,
				pageable.getPageNumber(), pageable.getPageSize());

		Page<Competitor> competitorPage = competitorRepository.findByIndustryIgnoreCase(industry, pageable);
		List<CompetitorResponse> competitorResponses = competitorMapper.toResponseList(competitorPage.getContent());

		return new CompetitorListResponse(competitorResponses, (int) competitorPage.getTotalElements(),
				competitorPage.getTotalPages(), competitorPage.getNumber(), competitorPage.getSize());
	}

	@Override
	@Transactional(readOnly = true)
	public List<String> getAllIndustries() {
		logger.info("Fetching all distinct industries");
		return competitorRepository.findAllDistinctIndustries();
	}

	@Override
	@Transactional(readOnly = true)
	public boolean existsByName(String name) {
		return competitorRepository.existsByNameIgnoreCase(name);
	}

	@Override
	@Transactional
	public void uploadCompetitorsFromExcel(MultipartFile file) throws Exception {
		List<Competitor> competitors = new ArrayList<>();

		try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rows = sheet.iterator();

			int rowNumber = 0;
			while (rows.hasNext()) {
				Row currentRow = rows.next();

				if (rowNumber == 0) {
					rowNumber++;
					continue;
				}

				Competitor competitor = new Competitor();
				competitor.setName(getCellValue(currentRow.getCell(0)));
				competitor.setDescription(getCellValue(currentRow.getCell(1)));
				competitor.setWebsite(getCellValue(currentRow.getCell(2)));
				competitor.setIndustry(getCellValue(currentRow.getCell(3)));

				competitors.add(competitor);
			}
		}

		competitorRepository.saveAll(competitors);
	}

	private String getCellValue(Cell cell) {
		if (cell == null)
			return null;
		switch (cell.getCellType()) {
		case STRING:
			return cell.getStringCellValue().trim();
		case NUMERIC:
			return String.valueOf(cell.getNumericCellValue());
		case BOOLEAN:
			return String.valueOf(cell.getBooleanCellValue());
		default:
			return null;
		}
	}
}
