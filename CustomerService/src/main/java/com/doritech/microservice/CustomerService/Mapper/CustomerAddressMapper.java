package com.doritech.microservice.CustomerService.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.doritech.microservice.CustomerService.Entity.CustomerAddress;
import com.doritech.microservice.CustomerService.Request.CustomerAddressRequest;
import com.doritech.microservice.CustomerService.Response.CustomerAddressResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerAddressMapper {

    // ---------------- Request -> Entity ----------------
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "type", source = "type", qualifiedByName = "stringToEnum")
    CustomerAddress toEntity(CustomerAddressRequest request);

    List<CustomerAddress> toEntityList(List<CustomerAddressRequest> requests);

    // ---------------- Entity -> Response ----------------
    @Mapping(target = "type", source = "type", qualifiedByName = "enumToString")
    CustomerAddressResponse toResponse(CustomerAddress address);

    List<CustomerAddressResponse> toResponseList(List<CustomerAddress> addresses);

    // ---------------- Custom Converters ----------------
    @Named("stringToEnum")
    default CustomerAddress.AddressType stringToEnum(String value) {
        if (value == null) return null;
        return CustomerAddress.AddressType.fromValue(value); // lowercase string -> enum
    }

    @Named("enumToString")
    default String enumToString(CustomerAddress.AddressType type) {
        return type != null ? type.getValue() : null; // enum -> lowercase string
    }
}
