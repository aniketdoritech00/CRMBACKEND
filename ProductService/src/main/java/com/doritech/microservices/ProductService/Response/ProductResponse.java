package com.doritech.microservices.ProductService.Response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class ProductResponse {
    private Long id;
    private String productCode;
    private String name;
    private String description;
    private String category;
    private BigDecimal price;
    private String currency;
    private LocalDateTime createdAt;
    
    // Constructors
    public ProductResponse() {}
    
    public ProductResponse(Long id, String productCode, String name, String description, 
                          String category, BigDecimal price, String currency, LocalDateTime createdAt) {
        this.id = id;
        this.productCode = productCode;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.currency = currency;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getProductCode() {
        return productCode;
    }
    
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductResponse that = (ProductResponse) o;
        return Objects.equals(id, that.id) && 
               Objects.equals(productCode, that.productCode);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, productCode);
    }
    
    @Override
    public String toString() {
        return "ProductResponse{" +
                "id=" + id +
                ", productCode='" + productCode + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", currency='" + currency + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}