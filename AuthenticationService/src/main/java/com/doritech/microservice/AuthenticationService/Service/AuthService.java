package com.doritech.microservice.AuthenticationService.Service;

import org.springframework.http.ResponseEntity;

import com.doritech.microservice.AuthenticationService.Request.LoginRequest;
import com.doritech.microservice.AuthenticationService.Response.ApiResponse;
import com.doritech.microservice.AuthenticationService.Response.LoginResponse;

public interface AuthService {

    ResponseEntity<ApiResponse<LoginResponse>> login(LoginRequest request);

    ResponseEntity<ApiResponse<Integer>> sendOTP(String email);

    ResponseEntity<ApiResponse<Boolean>> verifyOTP(String email, int enteredOtp);

    ResponseEntity<ApiResponse<Boolean>> updatePasswordByEmail(String email, String newPassword);
}
