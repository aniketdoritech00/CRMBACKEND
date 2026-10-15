package com.dritech.microservice.EmployeeService.Enum;

public enum Department {
    SALES("Sales"),
    MARKETING("Marketing"),
    HR("Human Resources"),
    IT("Information Technology"),
    FINANCE("Finance"),
    OPERATIONS("Operations"),
    CUSTOMER_SERVICE("Customer Service");
    
    private final String displayName;
    
    Department(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}