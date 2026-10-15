//package com.doritech.microservice.CustomerService.Config;
//
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.List;
//
//@Component
//public class JwtValidationFilter extends OncePerRequestFilter {
//
//    private final List<String> publicEndpoints = Arrays.asList(
//        "/register", "/forgot-password", "/health",  "/**"
//    );
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//
//        String path = request.getRequestURI();
//
//        if (isPublicEndpoint(path)) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String userEmail = request.getHeader("X-User-Email");
//
//        if (userEmail == null || userEmail.isEmpty()) {
//            sendErrorResponse(response, 401, "Authentication required");
//            return;
//        }
//
//        filterChain.doFilter(request, response);
//    }
//
//    private boolean isPublicEndpoint(String path) {
//        return publicEndpoints.stream().anyMatch(path::contains);
//    }
//
//    private void sendErrorResponse(HttpServletResponse response, int statusCode, String message)
//            throws IOException {
//        response.setStatus(statusCode);
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//
//        String jsonResponse = String.format(
//            "{\"statusCode\": %d, \"error\": \"Unauthorized\", \"message\": \"%s\"}",
//            statusCode, message
//        );
//        response.getWriter().write(jsonResponse);
//    }
//}