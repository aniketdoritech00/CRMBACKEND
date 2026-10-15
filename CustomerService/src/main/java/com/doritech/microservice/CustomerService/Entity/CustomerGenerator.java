package com.doritech.microservice.CustomerService.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "customer_generator")
public class CustomerGenerator {
 
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Integer id;
 
 @Column(name = "prefix")
 private String prefix;
 
 @Column(name = "suffix")
 private String suffix;
 
 // Constructors
 public CustomerGenerator() {}
 
 public CustomerGenerator(String prefix, String suffix) {
     this.prefix = prefix;
     this.suffix = suffix;
 }

public Integer getId() {
	return id;
}

public void setId(Integer id) {
	this.id = id;
}

public String getPrefix() {
	return prefix;
}

public void setPrefix(String prefix) {
	this.prefix = prefix;
}

public String getSuffix() {
	return suffix;
}

public void setSuffix(String suffix) {
	this.suffix = suffix;
}
 
 
}
 