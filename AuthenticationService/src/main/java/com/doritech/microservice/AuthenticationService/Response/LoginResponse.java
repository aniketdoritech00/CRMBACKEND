package com.doritech.microservice.AuthenticationService.Response;
public class LoginResponse {
    private String username;
    private String token;
    private String fullname;
    private String role;
  	private String phoneNumber;
  	private String position;
	public LoginResponse(String username, String token) {
		super();
		this.username = username;
		this.token = token;
	}
	
	

	public LoginResponse(String username, String token, String fullname, String role, String phoneNumber,
			String position) {
		super();
		this.username = username;
		this.token = token;
		this.fullname = fullname;
		this.role = role;
		this.phoneNumber = phoneNumber;
		this.position = position;
	}



	public String getFullname() {
		return fullname;
	}



	public void setFullname(String fullname) {
		this.fullname = fullname;
	}



	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
    
}