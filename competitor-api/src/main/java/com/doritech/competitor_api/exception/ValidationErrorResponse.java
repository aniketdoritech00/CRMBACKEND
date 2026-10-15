package com.doritech.competitor_api.exception;
import java.time.LocalDateTime;
import java.util.Map;

public class ValidationErrorResponse extends ErrorResponse {
    
    private Map<String, String> fieldErrors;
    
    public ValidationErrorResponse() {
        super();
    }
    
    public ValidationErrorResponse(String errorCode, String message, int status, 
                                 LocalDateTime timestamp, Map<String, String> fieldErrors) {
        super(errorCode, message, status, timestamp);
        this.fieldErrors = fieldErrors;
    }
    
    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
    
    public void setFieldErrors(Map<String, String> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
}