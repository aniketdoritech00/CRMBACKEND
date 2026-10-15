package com.doritech.role_based_menu.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doritech.role_based_menu.model.ResponseEntity;
import com.doritech.role_based_menu.service.MenuAccessService;

@RestController
@RequestMapping("/menu")
@CrossOrigin(allowedHeaders = "*")
public class MenuAccessController {

	private static final Logger logger = LoggerFactory.getLogger(MenuAccessController.class);

	@Autowired
	private MenuAccessService menuAccessService;

	@GetMapping("/getAllRoles")
	public ResponseEntity getAllRoles() {
		logger.info("Fetching all roles.");
		return menuAccessService.getAllRoles();
	}

	@PutMapping("/assignMenuDetailsToRole")
	public ResponseEntity assignMenuDetailsToRole(@RequestParam("empRole") String empRole,
			@RequestParam("menuId") int id) {
		logger.info("Assigning menu details to role: {} for menu ID: {}", empRole, id);
		return menuAccessService.assignMenuDetailsToRole(empRole, id);
	}

	@PutMapping("/revokeMenuDetailsFromTheRole")
	public ResponseEntity revokeMenuDetailsFromTheRole(@RequestParam("empRole") String empRole,
			@RequestParam("menuId") int id) {
		logger.info("Revoking menu details from role: {} for menu ID: {}", empRole, id);
		return menuAccessService.revokeMenuDetailsFromTheRole(empRole, id);
	}

	@GetMapping("/getMenuDetailsByRole")
	public ResponseEntity getMenuDetailsByRole(@RequestParam String empRole) throws Exception {
		logger.info("Fetching menu details for role: {}", empRole);
		return menuAccessService.getMenuDetailsByRole(empRole);
	}

}
