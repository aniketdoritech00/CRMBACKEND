package com.doritech.microservice.CustomerService.Response;

public class OrganizationResponseDTO {
   private Long id;
   private String name;
   private String shortCode;

   public Long getId() {
      return this.id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getShortCode() {
      return this.shortCode;
   }

   public void setShortCode(String shortCode) {
      this.shortCode = shortCode;
   }
}