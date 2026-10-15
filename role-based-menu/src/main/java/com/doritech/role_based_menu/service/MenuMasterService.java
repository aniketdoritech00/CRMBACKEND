package com.doritech.role_based_menu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.doritech.role_based_menu.common.Message;
import com.doritech.role_based_menu.model.MenuMasterDTO;
import com.doritech.role_based_menu.model.MenuMasterEntity;
import com.doritech.role_based_menu.model.ResponseEntity;
import com.doritech.role_based_menu.repository.MenuMasterRepository;

@Service
public class MenuMasterService {

	@Autowired
	private MenuMasterRepository menuMasterRepository;

	public ResponseEntity saveOrEditMenuDetails(MenuMasterDTO menuMasterDTO) {
		ResponseEntity response = new ResponseEntity();

		try {
			List<Message> messageList = this.validateMenuDetails(menuMasterDTO);
			MenuMasterEntity menuMaster = null;
			if (messageList.isEmpty()) {
				Optional<MenuMasterEntity> existingMenuMasterEntity = this.menuMasterRepository
						.findById(menuMasterDTO.getId());
				if (!existingMenuMasterEntity.isPresent()) {
					menuMaster = new MenuMasterEntity();
					menuMaster.setMenuName(menuMasterDTO.getMenuName());
					menuMaster.setMenuHandlerName(menuMasterDTO.getMenuHandlerName());
					menuMaster.setMenuIcon(menuMasterDTO.getMenuIcon());
					menuMaster.setParentId(menuMasterDTO.getParentId());
					this.menuMasterRepository.save(menuMaster);
					response.setPayload(menuMaster);
					response.setStatusCode(HttpStatus.OK.value());
					response.setMessage("New Menu Details saved successfully.");
					return response;
				} else {
					menuMaster = (MenuMasterEntity) existingMenuMasterEntity.get();
					menuMaster.setMenuName(menuMasterDTO.getMenuName());
					menuMaster.setMenuHandlerName(menuMasterDTO.getMenuHandlerName());
					menuMaster.setMenuIcon(menuMasterDTO.getMenuIcon());
					menuMaster.setParentId(menuMasterDTO.getParentId());
					this.menuMasterRepository.save(menuMaster);
					response.setPayload(menuMaster);
					response.setStatusCode(HttpStatus.OK.value());
					response.setMessage("Menu Details updated successfully.");
					return response;
				}
			} else {
				response.setStatusCode(HttpStatus.BAD_REQUEST.value());
				response.setMessage("Invalid data received");
				response.setPayload(messageList);
				return response;
			}
		} catch (Exception var6) {
			var6.printStackTrace();
			response.setStatusCode(HttpStatus.BAD_REQUEST.value());
			response.setMessage(var6.getMessage());
		}

		return response;
	}

	public ResponseEntity getAllMenuDetails() {
		ResponseEntity response = new ResponseEntity();

		try {
			List<MenuMasterEntity> menuMasterEntityList = this.menuMasterRepository.findAll();
			response.setPayload(menuMasterEntityList);
			response.setStatusCode(HttpStatus.OK.value());
			response.setMessage("All Menu Details fetched successfully.");
			return response;
		} catch (Exception var3) {
			var3.printStackTrace();
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setMessage("Unable to fetch data.");
		}

		return response;
	}

	public List<Message> validateMenuDetails(MenuMasterDTO menuMasterDTO) {
		List<Message> validationMessageList = new ArrayList<>();

		if (menuMasterDTO.getParentId() < 0 || menuMasterDTO.getParentId() > 1) {
			validationMessageList.add(new Message("Parent Id", "Parent Id is Invalid"));
		}

		if (menuMasterDTO.getMenuName() == null || StringUtils.isEmpty(menuMasterDTO.getMenuName())) {
			validationMessageList.add(new Message("Menu Name", "Menu Name can't be null or empty"));
		}

		if (menuMasterDTO.getMenuHandlerName() == null || StringUtils.isEmpty(menuMasterDTO.getMenuHandlerName())) {
			validationMessageList.add(new Message("Menu Handler Name", "Menu Handler Name can't be null or empty"));
		}

		if (menuMasterDTO.getMenuIcon() == null || StringUtils.isEmpty(menuMasterDTO.getMenuIcon())) {
			validationMessageList.add(new Message("Menu Icon", "Menu Icon can't be null or empty"));
		}

		return validationMessageList;
	}
}
