package com.doritech.competitor_api.exception;
public class CompetitorAlreadyExistsException extends RuntimeException {
    
    public CompetitorAlreadyExistsException(String message) {
        super(message);
    }
    
    public CompetitorAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}