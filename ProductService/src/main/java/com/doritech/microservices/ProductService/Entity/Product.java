// Product.java
package com.doritech.microservices.ProductService.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "product_code", unique = true, nullable = false, length = 50)
	private String productCode;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "description", columnDefinition = "TEXT")
	private String description;

	@Column(name = "category", length = 100)
	private String category;

	@Column(name = "price", precision = 15, scale = 2)
	private BigDecimal price;

	@Column(name = "currency", length = 3)
	private String currency = "INR";

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	// Constructors
	public Product() {
	}

	public Product(String productCode, String name, String description, String category, BigDecimal price,
			String currency) {
		this.productCode = productCode;
		this.name = name;
		this.description = description;
		this.category = category;
		this.price = price;
		this.currency = currency;
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

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Product product = (Product) o;
		return Objects.equals(id, product.id) && Objects.equals(productCode, product.productCode);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, productCode);
	}

	@Override
	public String toString() {
		return "Product{" + "id=" + id + ", productCode='" + productCode + '\'' + ", name='" + name + '\''
				+ ", category='" + category + '\'' + ", price=" + price + ", currency='" + currency + '\'' + '}';
	}
}
