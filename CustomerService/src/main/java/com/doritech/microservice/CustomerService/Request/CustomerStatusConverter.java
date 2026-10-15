package com.doritech.microservice.CustomerService.Request;

import com.doritech.microservice.CustomerService.Enum.CustomerStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CustomerStatusConverter implements AttributeConverter<CustomerStatus, String> {

    @Override
    public String convertToDatabaseColumn(CustomerStatus status) {
        return status == null ? null : status.name();
    }

    @Override
    public CustomerStatus convertToEntityAttribute(String dbData) {
        return dbData == null ? null : CustomerStatus.valueOf(dbData.toUpperCase());
    }
}
