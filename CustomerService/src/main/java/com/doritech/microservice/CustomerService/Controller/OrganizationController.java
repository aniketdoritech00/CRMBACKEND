package com.doritech.microservice.CustomerService.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doritech.microservice.CustomerService.Request.OrganizationRequestDTO;
import com.doritech.microservice.CustomerService.Response.OrganizationResponseDTO;
import com.doritech.microservice.CustomerService.Service.OrganizationService;

@RestController
@CrossOrigin(allowedHeaders = "*")
@RequestMapping({ "/api/organizations" })
public class OrganizationController {
	private final OrganizationService organizationService;

	OrganizationController(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

	@PostMapping
	public OrganizationResponseDTO create(@RequestBody OrganizationRequestDTO dto) {
		return this.organizationService.createOrganization(dto);
	}

	@GetMapping({ "/{id}" })
	public OrganizationResponseDTO getOne(@PathVariable Long id) {
		return this.organizationService.getOrganization(id);
	}

	@GetMapping
	public List<OrganizationResponseDTO> getAll() {
		return this.organizationService.getAllOrganizations();
	}

	@GetMapping({ "/all" })
	public List<OrganizationResponseDTO> getAllOrganization(int page, int size) {
		return this.organizationService.getAllOrganizations(page, size);
	}

	@PutMapping({ "/{id}" })
	public OrganizationResponseDTO update(@PathVariable Long id, @RequestBody OrganizationRequestDTO dto) {
		return this.organizationService.updateOrganization(id, dto);
	}

	@DeleteMapping({ "/{id}" })
	public void delete(@PathVariable Long id) {
		this.organizationService.deleteOrganization(id);
	}
}