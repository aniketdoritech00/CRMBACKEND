package com.doritech.microservice.AuthenticationService.Filter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.doritech.microservice.AuthenticationService.Service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtService jwtService;

	public JwtAuthenticationFilter(JwtService jwtService) {
		this.jwtService = jwtService;
	}

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {
		final String authHeader = request.getHeader("Authorization");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		try {
			final String jwt = authHeader.substring(7);
			final String userEmail = jwtService.getEmailId(jwt);
			System.out.println("UserEmail Id =" + userEmail);

			if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				if (jwtService.isTokenExpired(jwt)) {
					sendErrorResponse(response, HttpStatus.UNAUTHORIZED.value(), "JWT token has expired.");
					return;
				}

				if (jwtService.isTokenValid(jwt, userEmail)) {
					List<String> roles = jwtService.getRolesFromToken(jwt);
					List<SimpleGrantedAuthority> authorities = roles.stream().map(SimpleGrantedAuthority::new)
							.collect(Collectors.toList());

					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
							userEmail, null, authorities);
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				} else {
					sendErrorResponse(response, HttpStatus.FORBIDDEN.value(), "Invalid token.");
					return;
				}
			}

			filterChain.doFilter(request, response);
		} catch (BadCredentialsException e) {
			sendErrorResponse(response, HttpStatus.UNAUTHORIZED.value(), "Authentication failed: " + e.getMessage());
		} catch (IllegalArgumentException e) { 
			sendErrorResponse(response, HttpStatus.FORBIDDEN.value(), "Invalid token format.");
		} catch (Exception e) {
			sendErrorResponse(response, HttpStatus.FORBIDDEN.value(),
					"An unexpected error occurred: " + e.getMessage());
		}
	}

	/**
	 * Utility method to send a JSON error response using HttpStatus.
	 */
	private void sendErrorResponse(HttpServletResponse response, int statusCode, String message) throws IOException {
		response.setStatus(statusCode); 
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		String jsonResponse = String.format("{\"statusCode\": %d, \"error\": \"%s\", \"message\": \"%s\"}", statusCode,
				HttpStatus.valueOf(statusCode).getReasonPhrase(), message);

		response.getWriter().write(jsonResponse);
	}

}
