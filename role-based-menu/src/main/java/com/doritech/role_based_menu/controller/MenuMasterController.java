package com.doritech.role_based_menu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doritech.role_based_menu.model.MenuMasterDTO;
import com.doritech.role_based_menu.model.ResponseEntity;
import com.doritech.role_based_menu.service.MenuMasterService;

@RestController
@RequestMapping("/menu")
@CrossOrigin(allowedHeaders = "*")
public class MenuMasterController {

	@Autowired
	MenuMasterService menuMasterService;

	@PostMapping("/saveOrEditMenuDetails")
	public ResponseEntity saveOrEditMenuDetails(@RequestBody MenuMasterDTO menuMasterDTO) {
		return this.menuMasterService.saveOrEditMenuDetails(menuMasterDTO);
	}

	@GetMapping("/getAllMenuDetails")
	public ResponseEntity getAllMenuDetails() throws Exception {
		return this.menuMasterService.getAllMenuDetails();
	}
}
