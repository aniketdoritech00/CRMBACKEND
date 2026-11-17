package com.dritech.microservice.EmployeeService.Enum;

public enum Department {
	SALES("Sales"), PMG("PMG"), STORE("STORE"), LOGISTIC("LOGISTIC"), MARKETING("Marketing"), HR("Human Resources"),
	IT("Information Technology"), FINANCE("Finance"), OPERATIONS("Operations"), CUSTOMER_SERVICE("Customer Service"),
	FA("fa");

	private final String displayName;

	private Department(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	// $FF: synthetic method
	private static Department[] $values() {
		return new Department[] { SALES, PMG, STORE, LOGISTIC, MARKETING, HR, IT, FINANCE, OPERATIONS, CUSTOMER_SERVICE,
				FA };
	}
}