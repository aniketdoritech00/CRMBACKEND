package com.doritech.microservice.AuthenticationService.Service;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration:3600000}") 
    private long expiration;

    private Key getSignInKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public static final String EMPLOYEE = "employee";
    public static final String ROLE = "role";
    public static final String NAME = "name";
    public static final String EMPLOYEE_ID = "emp_id";
    public static final String EMPLOYEE_NAME = "emp_name";
    public static final String LOGIN_FLAG = "login_flag";
    public static final String BRANCH = "branch";
    public static final String DEVICE_SERIAL_NO = "device_serial_no";
    public static final String PHONE_NO = "phone_no";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String PROTECTION_TYPE = "protection_type";
    public static final String EMPLOYEE_ROLE = "emp_role";
    public static final String PASSCODE = "passcode";
    public static final String DEVICE_NAME = "device_name";

    public String generateTokenWithEmployeeData(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(EMAIL, username);
        return Jwts.builder()
                .setSubject(username)
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        Claims claims = extractAllClaims(token);
        String username = claims.getSubject();
        if (username == null) {
            username = claims.get(EMAIL, String.class);
        }
        return username;
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractRole(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get(EMPLOYEE_ROLE, String.class) != null ? claims.get(EMPLOYEE_ROLE, String.class) : "N/A";
    }

    public List<String> getRolesFromToken(String token) {
        Claims claims = extractAllClaims(token);
        Object rolesObject = claims.get(EMPLOYEE_ROLE);

        if (rolesObject instanceof String) {
            return Collections.singletonList((String) rolesObject);
        } else if (rolesObject instanceof List<?>) {
            return ((List<?>) rolesObject).stream()
                    .filter(String.class::isInstance)
                    .map(String.class::cast)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public String getEmailId(String token) {
        Claims claims = extractAllClaims(token);
        String email = claims.get(EMAIL, String.class);
        if (email == null) {
            email = claims.getSubject();
        }
        return email;
    }
    public String extractEmail(String token) {
        Claims claims = extractAllClaims(token);
        String email = claims.get(EMAIL, String.class);
        if (email == null) {
            email = claims.getSubject();
        }
        return email;
    }
    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    public boolean isTokenValid(String token, String userDetails) {
        try {
            final String username = extractUsername(token);
            return (username != null && username.equals(userDetails) && !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
}