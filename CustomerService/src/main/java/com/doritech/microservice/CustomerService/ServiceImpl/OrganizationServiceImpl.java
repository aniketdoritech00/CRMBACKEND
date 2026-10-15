package com.doritech.microservice.CustomerService.ServiceImpl;

import com.doritech.microservice.CustomerService.Entity.Organization;
import com.doritech.microservice.CustomerService.Repository.OrganizationRepository;
import com.doritech.microservice.CustomerService.Request.OrganizationRequestDTO;
import com.doritech.microservice.CustomerService.Response.OrganizationResponseDTO;
import com.doritech.microservice.CustomerService.Service.OrganizationService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrganizationServiceImpl implements OrganizationService {
   @Autowired
   private OrganizationRepository organizationRepo;

   public OrganizationResponseDTO createOrganization(OrganizationRequestDTO dto) {
      Organization org = new Organization();
      org.setName(dto.getName());
      org.setShortCode(dto.getShortCode());
      Organization saved = (Organization)this.organizationRepo.save(org);
      return this.toResponse(saved);
   }

   public OrganizationResponseDTO getOrganization(Long id) {
      Organization org = (Organization)this.organizationRepo.findById(id).orElseThrow(() -> new RuntimeException("Organization Not Found"));
      return this.toResponse(org);
   }

   public void deleteOrganization(Long id) {
      this.organizationRepo.deleteById(id);
   }

   private OrganizationResponseDTO toResponse(Organization org) {
      OrganizationResponseDTO dto = new OrganizationResponseDTO();
      dto.setId(org.getId());
      dto.setName(org.getName());
      dto.setShortCode(org.getShortCode());
      return dto;
   }

   public List<OrganizationResponseDTO> getAllOrganizations() {
      List<Organization> list = this.organizationRepo.findAll();
      return list.stream().map(this::toResponse).toList();
   }

   public List<OrganizationResponseDTO> getAllOrganizations(int page, int size) {
      Pageable pageable = PageRequest.of(page, size);
      Page<Organization> orgPage = this.organizationRepo.findAll(pageable);
      return orgPage.getContent().stream().map(this::toResponse).toList();
   }

   public OrganizationResponseDTO updateOrganization(Long id, OrganizationRequestDTO dto) {
      Organization org = (Organization)this.organizationRepo.findById(id).orElseThrow(() -> new RuntimeException("Organization not found with ID: " + id));
      org.setName(dto.getName());
      org.setShortCode(dto.getShortCode());
      Organization updated = (Organization)this.organizationRepo.save(org);
      return this.toResponse(updated);
   }
}