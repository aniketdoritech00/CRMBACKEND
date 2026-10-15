// CustomerProduct.java
package com.doritech.microservices.ProductService.Entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "customer_products")
public class CustomerProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "customer_id", nullable = false)
    private Long customerId;
    
    @Column(name = "product_id", nullable = false)
    private Long productId;
    
    @Column(name = "assigned_on", nullable = false)
    private LocalDate assignedOn;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProductStatus status = ProductStatus.ACTIVE;
    
    @Column(name = "quantity")
    private Integer quantity = 1;
    
    // Relationship with Product
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;
    
    public enum ProductStatus {
        ACTIVE, INACTIVE, TRIAL, EXPIRED
    }
    
    // Constructors
    public CustomerProduct() {}
    
    public CustomerProduct(Long customerId, Long productId, LocalDate assignedOn, 
                          ProductStatus status, Integer quantity) {
        this.customerId = customerId;
        this.productId = productId;
        this.assignedOn = assignedOn;
        this.status = status;
        this.quantity = quantity;
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
    
    public ProductStatus getStatus() {
        return status;
    }
    
    public void setStatus(ProductStatus status) {
        this.status = status;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public Product getProduct() {
        return product;
    }
    
    public void setProduct(Product product) {
        this.product = product;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerProduct that = (CustomerProduct) o;
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
        return "CustomerProduct{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", productId=" + productId +
                ", assignedOn=" + assignedOn +
                ", status=" + status +
                ", quantity=" + quantity +
                '}';
    }
}
