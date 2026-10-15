package com.doritech.microservices.ProductService.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import com.doritech.microservices.ProductService.Entity.CustomerProduct;
import com.doritech.microservices.ProductService.Request.CustomerProductRequest;
import com.doritech.microservices.ProductService.Response.CustomerProductResponse;

@Mapper(componentModel = "spring", uses = {ProductMapper.class }, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerProductMapper {

	@Mapping(target = "status", source = "status", qualifiedByName = "mapStatus")
	CustomerProductResponse toResponse(CustomerProduct customerProduct);

	@Mapping(target = "status", source = "status", qualifiedByName = "mapStatusEnum")
	CustomerProduct toEntity(CustomerProductRequest request);

	@Named("mapStatus")
	default String mapStatus(CustomerProduct.ProductStatus status) {
		return status != null ? status.name() : null;
	}

	@Named("mapStatusEnum")
	default CustomerProduct.ProductStatus mapStatusEnum(String status) {
		return status != null ? CustomerProduct.ProductStatus.valueOf(status.toUpperCase())
				: CustomerProduct.ProductStatus.ACTIVE;
	}
}