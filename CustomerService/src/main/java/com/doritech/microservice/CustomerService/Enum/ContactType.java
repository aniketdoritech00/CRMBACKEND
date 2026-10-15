package com.doritech.microservice.CustomerService.Enum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ContactType {
    DIRECT_INSTALLATION("Direct Installation"),
    DIRECT_AMC("Direct AMC"),
    INDIRECT_CONTRACT("Indirect-Contract"),
    EX_CLIENT("Ex-Client");

    private final String value;

    ContactType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static ContactType fromValue(String value) {
        for (ContactType type : ContactType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid ContactType: " + value);
    }
}
