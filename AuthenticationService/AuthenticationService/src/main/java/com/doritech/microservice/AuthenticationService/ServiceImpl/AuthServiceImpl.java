package com.doritech.microservice.AuthenticationService.ServiceImpl;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.doritech.microservice.AuthenticationService.Entity.Employee;
import com.doritech.microservice.AuthenticationService.Repository.EmployeeRepository;
import com.doritech.microservice.AuthenticationService.Request.LoginRequest;
import com.doritech.microservice.AuthenticationService.Response.ApiResponse;
import com.doritech.microservice.AuthenticationService.Response.LoginResponse;
import com.doritech.microservice.AuthenticationService.Service.AuthService;
import com.doritech.microservice.AuthenticationService.Service.JwtService;

@Service
public class AuthServiceImpl implements AuthService {
    
    @Autowired
    private EmployeeRepository employeeRepo;
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public ResponseEntity<ApiResponse<LoginResponse>> login(LoginRequest request) {
        try {
            System.out.println("Login attempt for email: " + request.getUsername());
            
            Optional<Employee> employeeOpt = employeeRepo.findByEmail(request.getUsername());
            
            if (employeeOpt.isEmpty()) {
                System.out.println(" User not found with email: " + request.getUsername());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("Invalid username or password"));
            }
            
            Employee employee = employeeOpt.get();
            System.out.println("User found: " + employee.getName() + " (" + employee.getEmail() + ")");
            System.out.println(" Stored password hash: " + employee.getPassword());
            System.out.println("Input password: " + request.getPassword());
            
            System.out.println(" Stored hash starts with: " + (employee.getPassword() != null ? employee.getPassword().substring(0, Math.min(10, employee.getPassword().length())) : "null"));
            System.out.println(" Input password length: " + (request.getPassword() != null ? request.getPassword().length() : 0));
            
            boolean passwordMatches = passwordEncoder.matches(request.getPassword(), employee.getPassword());
            System.out.println(" Password matches: " + passwordMatches);
            
            if (!passwordMatches) {
                System.out.println(" Password mismatch for user: " + request.getUsername());
                
                String encodedInput = passwordEncoder.encode(request.getPassword());
                System.out.println(" Input password would encode to: " + encodedInput);
                
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("Invalid username or password"));
            }
            
            System.out.println(" Password verified successfully for user: " + request.getUsername());
            
            // Generate JWT
            String token = jwtService.generateTokenWithEmployeeData(employee.getEmail());
            System.out.println(" Generated JWT token for user: " + request.getUsername());
            
            LoginResponse response = new LoginResponse(employee.getEmail(), token);
            
            return ResponseEntity.ok(ApiResponse.success("Login successful", response));
            
        } catch (Exception e) {
            System.err.println(" Login error for user " + request.getUsername() + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Something went wrong: " + e.getMessage()));
        }
    }
}