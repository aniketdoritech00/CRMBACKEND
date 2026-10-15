package com.doritech.microservice.CustomerService.Mapper;

import org.mapstruct.*;
import org.springframework.data.domain.Page;

import com.doritech.microservice.CustomerService.Entity.Customer;
import com.doritech.microservice.CustomerService.Entity.CustomerTag;
import com.doritech.microservice.CustomerService.Enum.CustomerStatus;
import com.doritech.microservice.CustomerService.Request.CustomerRequest;
import com.doritech.microservice.CustomerService.Response.CustomerResponse;
import com.doritech.microservice.CustomerService.Response.PagedResponse;
import com.doritech.microservice.CustomerService.Response.TagResponse;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {CustomerAddressMapper.class, ContactMapper.class, TagMapper.class})
public interface CustomerMapper {

    // ----------- Request -> Entity -----------
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "addresses", source = "addresses")
    @Mapping(target = "contacts", source = "contacts")
    @Mapping(target = "customerTags", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "customerCode", ignore = true)
    Customer toEntity(CustomerRequest request);

    // ----------- Entity -> Response -----------
    @Mapping(target = "status", source = "status", qualifiedByName = "statusToString")
    @Mapping(target = "tags", source = "customerTags", qualifiedByName = "customerTagsToTagResponses")
    CustomerResponse toResponse(Customer customer);

    List<CustomerResponse> toResponseList(List<Customer> customers);

    // ----------- Enum <-> String Conversion -----------
    @Named("statusToString")
    default String mapStatus(CustomerStatus status) {
        return status != null ? status.getValue() : null;
    }

    @Named("stringToStatus")
    default CustomerStatus mapStatus(String status) {
        return status != null ? CustomerStatus.fromValue(status) : null;
    }

    // ----------- CustomerTag -> TagResponse -----------
    @Named("customerTagsToTagResponses")
    default List<TagResponse> customerTagsToTagResponses(List<CustomerTag> customerTags) {
        if (customerTags == null) return null;
        return customerTags.stream()
                .map(ct -> new TagResponse(ct.getTag().getId(), ct.getTag().getName()))
                .collect(Collectors.toList());
    }

    // ----------- Update Entity (PATCH / PUT) -----------
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customerCode", ignore = true)
    @Mapping(target = "contacts", ignore = true)
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "customerTags", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(@MappingTarget Customer customer, CustomerRequest request);

    // ----------- Paging Response Wrapper -----------
    default PagedResponse<CustomerResponse> toPagedResponse(Page<Customer> page) {
        return PagedResponse.<CustomerResponse>builder()
                .content(toResponseList(page.getContent()))
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }
}