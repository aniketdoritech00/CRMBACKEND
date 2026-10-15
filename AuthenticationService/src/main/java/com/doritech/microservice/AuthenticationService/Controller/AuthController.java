package com.doritech.microservice.AuthenticationService.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doritech.microservice.AuthenticationService.Request.LoginRequest;
import com.doritech.microservice.AuthenticationService.Response.ApiResponse;
import com.doritech.microservice.AuthenticationService.Response.LoginResponse;
import com.doritech.microservice.AuthenticationService.Service.AuthService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(allowedHeaders = "*")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/login")
	public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
		return authService.login(request);
	}

	@GetMapping("/health")
	public ResponseEntity<String> health() {
		return ResponseEntity.ok("Auth Service is running");
	}

	@PostMapping("/send-otp")
	public ResponseEntity<ApiResponse<Integer>> sendOtp(@RequestParam("email") String email) {
		return authService.sendOTP(email);
	}

	@PostMapping("/verify-otp")
	public ResponseEntity<ApiResponse<Boolean>> verifyOtp(@RequestParam("email") String email,
			@RequestParam("otp") int enteredOtp) {
		return authService.verifyOTP(email, enteredOtp);
	}

	@PostMapping("/update-password")
	public ResponseEntity<ApiResponse<Boolean>> updatePassword(@RequestParam("email") String email,
			@RequestParam("newPassword") String newPassword) {
		return authService.updatePasswordByEmail(email, newPassword);
	}

	@PostMapping("/validate-token")
	public ResponseEntity<ApiResponse<String>> validateToken(@RequestHeader("Authorization") String authHeader) {
		try {
			if (authHeader == null || !authHeader.startsWith("Bearer ")) {
				return ResponseEntity.badRequest().body(ApiResponse.error("Invalid Authorization header"));
			}
			return ResponseEntity.ok(ApiResponse.success("Token is valid", "VALID"));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(ApiResponse.error("Token validation failed: " + e.getMessage()));
		}
	}
}
