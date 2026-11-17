package com.doritech.microservice.AuthenticationService.Response;

public class LoginResponse {
   private String id;
   private String username;
   private String token;
   private String fullname;
   private String role;
   private String phoneNumber;
   private String position;
   private String officeBranch;

   public String getId() {
      return this.id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getUsername() {
      return this.username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public String getToken() {
      return this.token;
   }

   public void setToken(String token) {
      this.token = token;
   }

   public String getFullname() {
      return this.fullname;
   }

   public void setFullname(String fullname) {
      this.fullname = fullname;
   }

   public String getRole() {
      return this.role;
   }

   public void setRole(String role) {
      this.role = role;
   }

   public String getPhoneNumber() {
      return this.phoneNumber;
   }

   public void setPhoneNumber(String phoneNumber) {
      this.phoneNumber = phoneNumber;
   }

   public String getPosition() {
      return this.position;
   }

   public void setPosition(String position) {
      this.position = position;
   }

   public String getOfficeBranch() {
      return this.officeBranch;
   }

   public void setOfficeBranch(String officeBranch) {
      this.officeBranch = officeBranch;
   }
}