//package com.doritech.microservice.ApiGateway.Service;
//
//import java.security.Key;
//import java.util.Collections;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//import org.springframework.core.env.Environment;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//
//@Service
//public class JwtService {
//
//	@Autowired
//	private Environment env;
//
//	// Helper to safely get secret key from config server properties
//	private String getSecretKey() {
//		String key = env.getProperty("jwt.secret");
//		if (key == null || key.isEmpty()) {
//			throw new IllegalArgumentException("jwt.secret property is missing or empty in environment");
//		}
//		return key;
//	}
//
//	// Helper to safely get expiration
//	private long getExpiration() {
//		String expStr = env.getProperty("jwt.expiration");
//		if (expStr == null || expStr.isEmpty()) {
//			throw new IllegalArgumentException("jwt.expiration property is missing or empty");
//		}
//		try {
//			return Long.parseLong(expStr);
//		} catch (NumberFormatException e) {
//			throw new IllegalArgumentException("jwt.expiration property is not a valid number");
//		}
//	}
//
//	private Key getSignInKey() {
//		return Keys.hmacShaKeyFor(getSecretKey().getBytes());
//	}
//
//	// Example claim fields
//	public static final String EMPLOYEE = "employee";
//	public static final String ROLE = "role";
//	public static final String NAME = "name";
//	public static final String EMPLOYEE_ID = "emp_id";
//	public static final String EMPLOYEE_NAME = "emp_name";
//	public static final String LOGIN_FLAG = "login_flag";
//	public static final String BRANCH = "branch";
//	public static final String DEVICE_SERIAL_NO = "device_serial_no";
//	public static final String PHONE_NO = "phone_no";
//	public static final String EMAIL = "email";
//	public static final String PASSWORD = "password";
//	public static final String PROTECTION_TYPE = "protection_type";
//	public static final String EMPLOYEE_ROLE = "emp_role";
//	public static final String PASSCODE = "passcode";
//	public static final String DEVICE_NAME = "device_name";
//
//	// Token generation method (custom claims can be added in claims Map)
//	public String generateTokenWithEmployeeData(String username) {
//		Map<String, Object> claims = new HashMap<>();
//		// Add relevant claims as needed, e.g. claims.put("email", username);
//		// For demo, left claim population empty
//
//		return Jwts.builder().setSubject(username).setClaims(claims).setIssuedAt(new Date())
//				.setExpiration(new Date(System.currentTimeMillis() + getExpiration()))
//				.signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
//	}
//
//	// Safely extract values
//	private String getValue(Object[] data, int index) {
//		return (data.length > index && data[index] != null) ? data[index].toString() : "N/A";
//	}
//
//	public String extractUsername(String token) {
//		return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody().get("email",
//				String.class);
//	}
//
//	public Claims extractAllClaims(String token) {
//		return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
//	}
//
//	public String extractRole(String token) {
//		Claims claims = extractAllClaims(token);
//		return claims.get(EMPLOYEE_ROLE, String.class) != null ? claims.get(EMPLOYEE_ROLE, String.class) : "N/A";
//	}
//
//	public List<String> getRolesFromToken(String token) {
//		Claims claims = extractAllClaims(token);
//		Object rolesObject = claims.get(EMPLOYEE_ROLE);
//
//		if (rolesObject instanceof String) {
//			return Collections.singletonList((String) rolesObject);
//		} else if (rolesObject instanceof List<?>) {
//			return ((List<?>) rolesObject).stream().filter(String.class::isInstance).map(String.class::cast)
//					.collect(Collectors.toList());
//		}
//		return Collections.emptyList();
//	}
//
//	public String getEmailId(String token) {
//		return extractAllClaims(token).get(EMAIL, String.class);
//	}
//
//	public Date extractExpiration(String token) {
//		return extractAllClaims(token).getExpiration();
//	}
//
//	public boolean isTokenExpired(String token) {
//		return extractExpiration(token).before(new Date());
//	}
//
//	public boolean isTokenValid(String token, String userDetails) {
//		final String username = extractUsername(token);
//		return (username.equals(userDetails) && !isTokenExpired(token));
//	}
//}