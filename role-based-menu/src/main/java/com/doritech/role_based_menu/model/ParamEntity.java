package com.doritech.role_based_menu.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "param_entity")
public class ParamEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String code;
	private String serial;
	private String descp1;
	private String descp2;
	private String descp3;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getDescp1() {
		return descp1;
	}

	public void setDescp1(String descp1) {
		this.descp1 = descp1;
	}

	public String getDescp2() {
		return descp2;
	}

	public void setDescp2(String descp2) {
		this.descp2 = descp2;
	}

	public String getDescp3() {
		return descp3;
	}

	public void setDescp3(String descp3) {
		this.descp3 = descp3;
	}

}
