package com.doritech.microservice.ApiGateway.Filter;

import com.doritech.microservice.ApiGateway.Util.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@Component
@Order(0)
public class JwtAuthenticationFilter implements GlobalFilter {

    private final JwtUtil jwtUtil;

    private final List<String> publicEndpoints = Arrays.asList(
        "/auth/login",
        "/auth/health",
        "/auth/validate-token",
        "/customers/register",
        "/customers/forgot-password",
        "/customer/register",
        "/customer/forgot-password",
        "/actuator/health",
        "/actuator/info",
        "/auth/send-otp",
        "/auth/verify-otp",
        "/auth/update-password",
        "/auth/forgot-password",
        "/auth/reset-password"

    );

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        if (isPublicEndpoint(path)) {
            return chain.filter(exchange);
        }

        String authorizationHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (!StringUtils.hasText(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
            return handleUnauthorized(exchange, "Missing or invalid Authorization header");
        }

        String token = authorizationHeader.substring(7);

        return validateTokenLocally(token, exchange, chain)
            .timeout(Duration.ofSeconds(5))
            .onErrorResume(error -> handleUnauthorized(exchange, "Authentication failed"));
    }

    private Mono<Void> validateTokenLocally(String token, ServerWebExchange exchange, GatewayFilterChain chain) {
        return Mono.fromCallable(() -> {
            if (!jwtUtil.validateToken(token)) {
                throw new RuntimeException("Invalid token");
            }
            if (jwtUtil.isTokenExpired(token)) {
                throw new RuntimeException("Token expired");
            }
            return jwtUtil.getUsernameFromToken(token);
        }).flatMap(username -> {
            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                    .headers(headers -> {
                        headers.set("X-User-Email", username);
                        headers.set("X-Authenticated", "true");
                    })
                    .build();

            ServerWebExchange modifiedExchange = exchange.mutate().request(modifiedRequest).build();
            return chain.filter(modifiedExchange);
        });
    }

    private boolean isPublicEndpoint(String path) {
        return publicEndpoints.stream().anyMatch(endpoint -> path.equals(endpoint) || path.startsWith(endpoint + "/"));
    }

    private Mono<Void> handleUnauthorized(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", "application/json");

        String body = String.format(
            "{\"statusCode\":401,\"error\":\"Unauthorized\",\"message\":\"%s\",\"path\":\"%s\"}",
            message, exchange.getRequest().getURI().getPath()
        );

        org.springframework.core.io.buffer.DataBuffer buffer = response.bufferFactory().wrap(body.getBytes());
        return response.writeWith(Mono.just(buffer));
    }
}