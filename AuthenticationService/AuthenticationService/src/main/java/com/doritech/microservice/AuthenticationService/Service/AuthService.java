package com.doritech.microservice.AuthenticationService.Service;

import org.springframework.http.ResponseEntity;

import com.doritech.microservice.AuthenticationService.Request.LoginRequest;
import com.doritech.microservice.AuthenticationService.Response.ApiResponse;
import com.doritech.microservice.AuthenticationService.Response.LoginResponse;

public interface AuthService {

	ResponseEntity<ApiResponse<LoginResponse>> login(LoginRequest request);
}