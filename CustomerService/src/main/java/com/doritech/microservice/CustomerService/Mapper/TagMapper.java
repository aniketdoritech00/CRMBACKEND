package com.doritech.microservice.CustomerService.Mapper;


import org.mapstruct.Mapper;

import com.doritech.microservice.CustomerService.Entity.Tag;
import com.doritech.microservice.CustomerService.Response.TagResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TagMapper {
    TagResponse toResponse(Tag tag);
    List<TagResponse> toResponseList(List<Tag> tags);

}