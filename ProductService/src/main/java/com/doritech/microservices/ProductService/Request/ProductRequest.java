// ProductRequest.java
package com.doritech.microservices.ProductService.Request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Objects;

public class ProductRequest {
    @NotBlank(message = "Product code is required")
    @Size(max = 50, message = "Product code must not exceed 50 characters")
    private String productCode;
    
    @NotBlank(message = "Product name is required")
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
    private String currency = "INR";
    
    // Constructors
    public ProductRequest() {}
    
    public ProductRequest(String productCode, String name, String description, 
                         String category, BigDecimal price, String currency) {
        this.productCode = productCode;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.currency = currency;
    }
    
    // Getters and Setters
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
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductRequest that = (ProductRequest) o;
        return Objects.equals(productCode, that.productCode) && 
               Objects.equals(name, that.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(productCode, name);
    }
    
    @Override
    public String toString() {
        return "ProductRequest{" +
                "productCode='" + productCode + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", currency='" + currency + '\'' +
                '}';
    }
}