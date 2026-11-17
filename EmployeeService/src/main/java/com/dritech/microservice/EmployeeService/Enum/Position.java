package com.dritech.microservice.EmployeeService.Enum;

public enum Position {
	MANAGER("Manager"), SENIOR_EXECUTIVE("Senior Executive"), EXECUTIVE("Executive"), TEAM_LEAD("Team Lead"),
	ASSOCIATE("Associate"), INTERN("Intern"), FA("fa");

	private final String displayName;

	private Position(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	// $FF: synthetic method
	private static Position[] $values() {
		return new Position[] { MANAGER, SENIOR_EXECUTIVE, EXECUTIVE, TEAM_LEAD, ASSOCIATE, INTERN, FA };
	}
}