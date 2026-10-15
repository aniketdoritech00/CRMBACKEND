package com.dritech.microservice.EmployeeService.Mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.dritech.microservice.EmployeeService.Entity.Employee;
import com.dritech.microservice.EmployeeService.Request.EmployeeRequest;
import com.dritech.microservice.EmployeeService.Response.EmployeeResponse;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EmployeeMapper {

	Employee toEntity(EmployeeRequest request);

	EmployeeResponse toResponse(Employee employee);

	List<EmployeeResponse> toResponseList(List<Employee> employees);

	void updateEntityFromRequest(EmployeeRequest request, @MappingTarget Employee employee);
}