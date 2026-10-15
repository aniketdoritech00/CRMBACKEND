package com.doritech.microservice.CustomerService.Mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.doritech.microservice.CustomerService.Entity.Contact;
import com.doritech.microservice.CustomerService.Request.ContactRequest;
import com.doritech.microservice.CustomerService.Response.ContactResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ContactMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Contact toEntity(ContactRequest request);
    
    ContactResponse toResponse(Contact contact);
    
    List<Contact> toEntityList(List<ContactRequest> requests);
    
    List<ContactResponse> toResponseList(List<Contact> contacts);
}