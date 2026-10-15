package com.doritech.microservice.CustomerService.Request;

import com.doritech.microservice.CustomerService.Enum.ContactType;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class ContactTypeConverter implements AttributeConverter<ContactType, String> {

	@Override
	public String convertToDatabaseColumn(ContactType attribute) {
		if (attribute == null) {
			return null;
		}
		return attribute.getValue(); // Store enum value (e.g., "Direct AMC")
	}

	@Override
	public ContactType convertToEntityAttribute(String dbData) {
		if (dbData == null) {
			return null;
		}
		return ContactType.fromValue(dbData); // Convert DB string to enum
	}
}
