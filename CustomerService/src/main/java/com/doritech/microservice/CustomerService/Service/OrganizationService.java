package com.doritech.microservice.CustomerService.Service;

import com.doritech.microservice.CustomerService.Request.OrganizationRequestDTO;
import com.doritech.microservice.CustomerService.Response.OrganizationResponseDTO;
import java.util.List;

public interface OrganizationService {
   OrganizationResponseDTO createOrganization(OrganizationRequestDTO dto);

   OrganizationResponseDTO getOrganization(Long id);

   List<OrganizationResponseDTO> getAllOrganizations();

   List<OrganizationResponseDTO> getAllOrganizations(int page, int size);

   OrganizationResponseDTO updateOrganization(Long id, OrganizationRequestDTO dto);

   void deleteOrganization(Long id);
}