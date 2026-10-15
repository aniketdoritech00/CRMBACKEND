package com.doritech.microservice.ApiGateway.Util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    public static final String EMAIL = "email";
    public static final String EMPLOYEE_ROLE = "emp_role";

    private Key getSignInKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUsernameFromToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            String username = claims.getSubject();
            if (username == null || username.isEmpty()) {
                username = claims.get(EMAIL, String.class);
            }
            return username;
        } catch (Exception e) {
            System.err.println("Error extracting username from token: " + e.getMessage());
            return null;
        }
    }

    public String getEmailFromToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            String email = claims.get(EMAIL, String.class);
            if (email == null || email.isEmpty()) {
                email = claims.getSubject();
            }
            return email;
        } catch (Exception e) {
            System.err.println("Error extracting email from token: " + e.getMessage());
            return null;
        }
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public boolean isTokenExpired(String token) {
        try {
            Date expiration = extractExpiration(token);
            return expiration.before(new Date());
        } catch (Exception e) {
            System.err.println("Error checking token expiration: " + e.getMessage());
            return true;
        }
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            
            String username = claims.getSubject();
            String email = claims.get(EMAIL, String.class);
            
            if ((username == null || username.isEmpty()) && 
                (email == null || email.isEmpty())) {
                System.err.println("Token missing both subject and email claims");
                return false;
            }
            
            if (isTokenExpired(token)) {
                System.err.println("Token has expired");
                return false;
            }
            
            return true;
        } catch (Exception e) {
            System.err.println("Token validation failed: " + e.getMessage());
            return false;
        }
    }

    public String getRoleFromToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return claims.get(EMPLOYEE_ROLE, String.class);
        } catch (Exception e) {
            System.err.println("Error extracting role from token: " + e.getMessage());
            return null;
        }
    }
}