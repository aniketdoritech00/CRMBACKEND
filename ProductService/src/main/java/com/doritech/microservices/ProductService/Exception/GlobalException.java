package com.doritech.microservices.ProductService.Exception;

public class GlobalException extends RuntimeException {
    public GlobalException(String message) {
        super(message);
    }

}
