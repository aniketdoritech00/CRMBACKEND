package com.doritech.microservices.ProductService.Response;

import java.time.LocalDate;
import java.util.Objects;

public class CustomerProductResponse {
    private Long id;
    private Long customerId;
    private Long productId;
    private LocalDate assignedOn;
    private String status;
    private Integer quantity;
    private ProductResponse product;
    
    // Constructors
    public CustomerProductResponse() {}
    
    public CustomerProductResponse(Long id, Long customerId, Long productId, LocalDate assignedOn, 
                                  String status, Integer quantity, ProductResponse product) {
        this.id = id;
        this.customerId = customerId;
        this.productId = productId;
        this.assignedOn = assignedOn;
        this.status = status;
        this.quantity = quantity;
        this.product = product;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
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
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public ProductResponse getProduct() {
        return product;
    }
    
    public void setProduct(ProductResponse product) {
        this.product = product;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerProductResponse that = (CustomerProductResponse) o;
        return Objects.equals(id, that.id) && 
               Objects.equals(customerId, that.customerId) && 
               Objects.equals(productId, that.productId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, customerId, productId);
    }
    
    @Override
    public String toString() {
        return "CustomerProductResponse{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", productId=" + productId +
                ", assignedOn=" + assignedOn +
                ", status='" + status + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
