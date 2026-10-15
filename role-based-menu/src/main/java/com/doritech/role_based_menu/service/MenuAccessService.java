package com.doritech.role_based_menu.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.doritech.role_based_menu.model.MenuAccessEntity;
import com.doritech.role_based_menu.model.MenuMasterEntity;
import com.doritech.role_based_menu.model.ParamEntity;
import com.doritech.role_based_menu.model.ResponseEntity;
import com.doritech.role_based_menu.repository.MenuAccessRepository;
import com.doritech.role_based_menu.repository.MenuMasterRepository;
import com.doritech.role_based_menu.repository.ParamRepository;

@Service
public class MenuAccessService {
	private static final Logger logger = LoggerFactory.getLogger(MenuAccessService.class);
	@Autowired
	MenuAccessRepository menuAccessRepository;
	@Autowired
	ParamRepository paramRepo;
	@Autowired
	MenuMasterRepository menuMasterRepository;
	@Autowired
	MenuMasterService menuMasterService;

	public ResponseEntity assignMenuDetailsToRole(String empRole, int id) {
		ResponseEntity response = new ResponseEntity();

		try {
			Optional<MenuMasterEntity> menuMasterOptional = this.menuMasterRepository.findById(id);
			if (menuMasterOptional.isPresent()) {
				MenuMasterEntity menu = (MenuMasterEntity) menuMasterOptional.get();
				this.menuMasterRepository.save(menu);
				MenuAccessEntity roleMaster = new MenuAccessEntity();
				roleMaster.setRoleName(empRole);
				roleMaster.setRoleId(this.getRoleIdByEmpRole(empRole));
				roleMaster.setMenuId(id);
				this.menuAccessRepository.save(roleMaster);
				response.setPayload(roleMaster);
				response.setStatusCode(HttpStatus.OK.value());
				response.setMessage("Menu Details assigned to the given Role successfully.");
				return response;
			} else {
				response.setStatusCode(HttpStatus.BAD_REQUEST.value());
				response.setMessage("Menu Details are already assigned to a Role.");
				return response;
			}
		} catch (Exception var7) {
			var7.printStackTrace();
			response.setMessage("Error occurred in assigning Menu Details: " + var7.getLocalizedMessage());
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}

		return response;
	}

	public ResponseEntity revokeMenuDetailsFromTheRole(String empRole, int id) {
		ResponseEntity response = new ResponseEntity();

		try {
			Optional<MenuMasterEntity> menuMasterEntity = this.menuMasterRepository.findById(id);
			if (menuMasterEntity.isPresent()) {
				MenuAccessEntity role = this.menuAccessRepository.findByRoleNameAndMenuId(empRole, id);
				if (role != null) {
					response.setPayload(role);
					this.menuAccessRepository.delete(role);
					MenuMasterEntity menu = (MenuMasterEntity) menuMasterEntity.get();
					this.menuMasterRepository.save(menu);
					response.setMessage("Menu Details revoked from the Role successfully");
					response.setStatusCode(HttpStatus.OK.value());
					return response;
				} else {
					response.setStatusCode(HttpStatus.BAD_REQUEST.value());
					response.setMessage("Menu Details not found for the specified Role and Menu ID");
					return response;
				}
			} else {
				response.setStatusCode(HttpStatus.BAD_REQUEST.value());
				response.setMessage("Provided data is not correct");
				return response;
			}
		} catch (Exception var7) {
			var7.printStackTrace();
			response.setMessage("Error occurred in revoking Menu Details: " + var7.getLocalizedMessage());
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}

		return response;
	}

	public ResponseEntity getMenuDetailsByRole(String empRole) throws Exception {
		ResponseEntity response = new ResponseEntity();

		try {
			List<MenuAccessEntity> menuDetailsByRole = this.menuAccessRepository.findByRoleName(empRole);
			List<MenuMasterEntity> allMenuDetails = this.menuMasterRepository.findAll();
			List<MenuMasterEntity> responseList = new ArrayList();
			Iterator var6 = allMenuDetails.iterator();

			while (var6.hasNext()) {
				MenuMasterEntity menuMaster = (MenuMasterEntity) var6.next();
				Iterator var8 = menuDetailsByRole.iterator();

				while (var8.hasNext()) {
					MenuAccessEntity menuAccess = (MenuAccessEntity) var8.next();
					if (menuAccess.getMenuId() == menuMaster.getId()) {
						responseList.add(menuMaster);
					}
				}
			}

			if (responseList != null) {
				response.setPayload(responseList);
				response.setMessage("Menu Details Based on Role fetched successfully.");
				response.setStatusCode(HttpStatus.OK.value());
				return response;
			} else {
				response.setMessage("No Menu Details are present with the given role.");
				response.setStatusCode(HttpStatus.NOT_FOUND.value());
				return response;
			}
		} catch (Exception var10) {
			var10.printStackTrace();
			response.setMessage("Unable to fetch Menu Details");
			response.setStatusCode(HttpStatus.NOT_FOUND.value());
		}

		return response;
	}

	private int getRoleIdByEmpRole(String empRole) {
		if (empRole.equalsIgnoreCase("ADMIN")) {
			return 101;
		} else if (empRole.equalsIgnoreCase("EMPLOYEE")) {
			return 102;
		} else if (empRole.equalsIgnoreCase("SUPERADMIN")) {
			return 103;
		}else {
			return 0;
		}
	}

	public ResponseEntity getAllRoles() {
		ResponseEntity response = new ResponseEntity();

		try {
			List<ParamEntity> roles = new ArrayList();
			List<ParamEntity> allParams = this.paramRepo.findAll();
			Iterator var4 = allParams.iterator();

			while (var4.hasNext()) {
				ParamEntity param = (ParamEntity) var4.next();
				if (param.getCode().equals("ROLE")) {
					roles.add(param);
				}
			}

			response.setPayload(roles);
			response.setMessage("All Roles fetched successfully.");
			response.setStatusCode(HttpStatus.OK.value());
			return response;
		} catch (Exception var6) {
			var6.printStackTrace();
			response.setStatusCode(HttpStatus.NOT_FOUND.value());
			response.setMessage("Roles not found");

		}

		return response;
	}
}
