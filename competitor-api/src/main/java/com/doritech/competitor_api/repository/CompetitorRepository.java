package com.doritech.competitor_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.doritech.competitor_api.model.Competitor;

@Repository
public interface CompetitorRepository extends JpaRepository<Competitor, Long> {

	Optional<Competitor> findByNameIgnoreCase(String name);

	List<Competitor> findByIndustryIgnoreCase(String industry);

	Page<Competitor> findByNameContainingIgnoreCase(String name, Pageable pageable);

	@Query("SELECT c FROM Competitor c WHERE " + "LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR "
			+ "LOWER(c.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR "
			+ "LOWER(c.industry) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
	Page<Competitor> searchCompetitors(@Param("searchTerm") String searchTerm, Pageable pageable);

	Page<Competitor> findByIndustryIgnoreCase(String industry, Pageable pageable);

	boolean existsByNameIgnoreCase(String name);

	@Query("SELECT DISTINCT c.industry FROM Competitor c WHERE c.industry IS NOT NULL ORDER BY c.industry")
	List<String> findAllDistinctIndustries();
}