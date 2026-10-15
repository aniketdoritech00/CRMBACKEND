package com.doritech.microservice.CustomerService.Enum;

public enum CommunicationMode {
    WHATSAPP("WhatsApp"),
    CALL("Call"),
    EMAIL("Email");

    private final String value;

    CommunicationMode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static CommunicationMode fromValue(String value) {
        if (value == null) return null;
        for (CommunicationMode mode : values()) {
            if (mode.getValue().equalsIgnoreCase(value)) {
                return mode;
            }
        }
        throw new IllegalArgumentException("Invalid communication mode: " + value);
    }
}
