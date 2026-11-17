package com.doritech.microservice.AuthenticationService.Service;

import com.doritech.microservice.AuthenticationService.Request.EmployeeDTO;
import com.doritech.microservice.AuthenticationService.Request.LoginRequest;
import com.doritech.microservice.AuthenticationService.Response.ApiResponse;
import com.doritech.microservice.AuthenticationService.Response.LoginResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
   ResponseEntity<ApiResponse<LoginResponse>> login(LoginRequest request);

   ResponseEntity<ApiResponse<Integer>> sendOTP(String email) throws Throwable;

   ResponseEntity<ApiResponse<Boolean>> verifyOTP(String email, int enteredOtp);

   ResponseEntity<ApiResponse<Boolean>> updatePasswordByEmail(String email, String newPassword);

   ResponseEntity<ApiResponse<String>> resetPassword(EmployeeDTO req);
}