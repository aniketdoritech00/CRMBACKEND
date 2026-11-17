package com.dritech.microservice.EmployeeService.Enum;

public enum EmployeeStatus {
	ACTIVE("Active"), INACTIVE("Inactive"), ON_LEAVE("On Leave"), TERMINATED("Terminated");

	private final String displayName;

	private EmployeeStatus(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	// $FF: synthetic method
	private static EmployeeStatus[] $values() {
		return new EmployeeStatus[] { ACTIVE, INACTIVE, ON_LEAVE, TERMINATED };
	}
}