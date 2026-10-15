package com.doritech.microservices.ProductService.Request;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.Objects;

public class CustomerProductRequest {
    @NotNull(message = "Customer ID is required")
    private Long customerId;
    
    @NotNull(message = "Product ID is required")
    private Long productId;
    
    private LocalDate assignedOn = LocalDate.now();
    
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity = 1;
    
    private String status = "ACTIVE";
    
    // Constructors
    public CustomerProductRequest() {}
    
    public CustomerProductRequest(Long customerId, Long productId, LocalDate assignedOn, 
                                 Integer quantity, String status) {
        this.customerId = customerId;
        this.productId = productId;
        this.assignedOn = assignedOn;
        this.quantity = quantity;
        this.status = status;
    }
    
    // Getters and Setters
    public Long getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    
    public LocalDate getAssignedOn() {
        return assignedOn;
    }
    
    public void setAssignedOn(LocalDate assignedOn) {
        this.assignedOn = assignedOn;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerProductRequest that = (CustomerProductRequest) o;
        return Objects.equals(customerId, that.customerId) && 
               Objects.equals(productId, that.productId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(customerId, productId);
    }
    
    @Override
    public String toString() {
        return "CustomerProductRequest{" +
                "customerId=" + customerId +
                ", productId=" + productId +
                ", assignedOn=" + assignedOn +
                ", quantity=" + quantity +
                ", status='" + status + '\'' +
                '}';
    }
}