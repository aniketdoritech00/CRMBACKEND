package com.doritech.microservice.AuthenticationService.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.doritech.microservice.AuthenticationService.Request.LoginRequest;
import com.doritech.microservice.AuthenticationService.Response.ApiResponse;
import com.doritech.microservice.AuthenticationService.Response.LoginResponse;
import com.doritech.microservice.AuthenticationService.Service.AuthService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    private final AuthService authService;
    
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    
//    @PostMapping("/login")
//    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
//        return authService.login(request);
//    }
    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Auth Service is running");
    }
    
    @PostMapping("/validate-token")
    public ResponseEntity<ApiResponse<String>> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid Authorization header"));
            }
            return ResponseEntity.ok(ApiResponse.success("Token is valid", "VALID"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Token validation failed: " + e.getMessage()));
        }
    }
}