package com.doritech.microservice.CustomerService.Request;

import com.doritech.microservice.CustomerService.Entity.CustomerAddress;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AddressTypeConverter implements AttributeConverter<CustomerAddress.AddressType, String> {

    @Override
    public String convertToDatabaseColumn(CustomerAddress.AddressType attribute) {
        return attribute == null ? null : attribute.getValue();  // lowercase store
    }

    @Override
    public CustomerAddress.AddressType convertToEntityAttribute(String dbData) {
        return dbData == null ? null : CustomerAddress.AddressType.fromValue(dbData);
    }
}
