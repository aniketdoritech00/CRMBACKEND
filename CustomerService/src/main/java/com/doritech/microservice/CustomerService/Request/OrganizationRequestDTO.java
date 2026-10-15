package com.doritech.microservice.CustomerService.Request;

public class OrganizationRequestDTO {
   private String name;
   private String shortCode;

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