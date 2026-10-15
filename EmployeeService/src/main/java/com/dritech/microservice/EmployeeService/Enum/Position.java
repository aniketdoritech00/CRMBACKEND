package com.dritech.microservice.EmployeeService.Enum;

public enum Position {
    MANAGER("Manager"),
    SENIOR_EXECUTIVE("Senior Executive"),
    EXECUTIVE("Executive"),
    TEAM_LEAD("Team Lead"),
    ASSOCIATE("Associate"),
    INTERN("Intern");
    
    private final String displayName;
    
    Position(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}