package com.doritech.microservices.ProductService.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.doritech.microservices.ProductService.Entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	Optional<Product> findByProductCode(String productCode);

	boolean existsByProductCode(String productCode);

	Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

	Page<Product> findByCategory(String category, Pageable pageable);

	@Query("SELECT p FROM Product p WHERE "
			+ "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND "
			+ "(:category IS NULL OR p.category = :category)")
	Page<Product> findByFilters(@Param("name") String name, @Param("category") String category, Pageable pageable);

	@Query(value = """
			SELECT *
			FROM product_service.products p
			WHERE
			    (:search IS NULL OR :search = ''
			    OR LOWER(p.product_code) LIKE LOWER(CONCAT('%', :search, '%'))
			    OR LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%'))
			    OR LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%'))
			    OR LOWER(p.category) LIKE LOWER(CONCAT('%', :search, '%'))
			    OR LOWER(p.currency) LIKE LOWER(CONCAT('%', :search, '%'))
			    OR CAST(p.price AS CHAR) LIKE CONCAT('%', :search, '%'))
			    ORDER BY p.price DESC
			""", countQuery = """
			SELECT COUNT(*)
			FROM product_service.products p
			WHERE
			    (:search IS NULL OR :search = ''
			    OR LOWER(p.product_code) LIKE LOWER(CONCAT('%', :search, '%'))
			    OR LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%'))
			    OR LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%'))
			    OR LOWER(p.category) LIKE LOWER(CONCAT('%', :search, '%'))
			    OR LOWER(p.currency) LIKE LOWER(CONCAT('%', :search, '%'))
			    OR CAST(p.price AS CHAR) LIKE CONCAT('%', :search, '%'))
			""", nativeQuery = true)
	Page<Product> searchAll(@Param("search") String search, Pageable pageable);

	@Query("SELECT DISTINCT p.category FROM Product p WHERE p.category IS NOT NULL ORDER BY p.category")
	List<String> findAllCategories();
	
	
}
