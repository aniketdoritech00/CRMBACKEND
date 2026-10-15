package com.doritech.microservices.ProductService.Request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Objects;

public class ProductUpdateRequest {
    @Size(max = 255, message = "Product name must not exceed 255 characters")
    private String name;
    
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;
    
    @Size(max = 100, message = "Category must not exceed 100 characters")
    private String category;
    
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Digits(integer = 13, fraction = 2, message = "Price format is invalid")
    private BigDecimal price;
    
    @Size(max = 3, message = "Currency must be 3 characters")
    private String currency;
    
    // Constructors
    public ProductUpdateRequest() {}
    
    public ProductUpdateRequest(String name, String description, String category, 
                               BigDecimal price, String currency) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.currency = currency;
    }
    
    // Getters and Setters
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
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductUpdateRequest that = (ProductUpdateRequest) o;
        return Objects.equals(name, that.name) && 
               Objects.equals(category, that.category);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, category);
    }
    
    @Override
    public String toString() {
        return "ProductUpdateRequest{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", currency='" + currency + '\'' +
                '}';
    }
}