package com.doritech.microservice.AuthenticationService.Service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.doritech.microservice.AuthenticationService.Entity.Employee;
import com.doritech.microservice.AuthenticationService.Repository.EmployeeRepository;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    private final EmployeeRepository employeeRepository;
    
    public CustomUserDetailsService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        
        return User.builder()
                .username(employee.getEmail())
                .password(employee.getPassword())
                .authorities(new ArrayList<>()) 
                .build();
    }
}