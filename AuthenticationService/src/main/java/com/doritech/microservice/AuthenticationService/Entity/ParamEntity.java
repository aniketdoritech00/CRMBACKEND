package com.doritech.microservice.AuthenticationService.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ParamEntity {
   @Id
   @GeneratedValue(
      strategy = GenerationType.IDENTITY
   )
   private Long id;
   private String code;
   private String series;
   private String value1;
   private String value2;

   public Long getId() {
      return this.id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getCode() {
      return this.code;
   }

   public void setCode(String code) {
      this.code = code;
   }

   public String getSeries() {
      return this.series;
   }

   public void setSeries(String series) {
      this.series = series;
   }

   public String getValue1() {
      return this.value1;
   }

   public void setValue1(String value1) {
      this.value1 = value1;
   }

   public String getValue2() {
      return this.value2;
   }

   public void setValue2(String value2) {
      this.value2 = value2;
   }
}