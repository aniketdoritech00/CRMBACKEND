package com.doritech.microservices.ProductService.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.doritech.microservices.ProductService.Entity.Product;
import com.doritech.microservices.ProductService.Request.ProductRequest;
import com.doritech.microservices.ProductService.Request.ProductUpdateRequest;
import com.doritech.microservices.ProductService.Response.ProductResponse;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

	ProductResponse toResponse(Product product);

	Product toEntity(ProductRequest request);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "productCode", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	void updateEntityFromRequest(ProductUpdateRequest request, @MappingTarget Product product);
}