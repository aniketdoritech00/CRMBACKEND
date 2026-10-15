package com.doritech.competitor_api.exception;
public class CompetitorNotFoundException extends RuntimeException {
    
    public CompetitorNotFoundException(String message) {
        super(message);
    }
    
    public CompetitorNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}