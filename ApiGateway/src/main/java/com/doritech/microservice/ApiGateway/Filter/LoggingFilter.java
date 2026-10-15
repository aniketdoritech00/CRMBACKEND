package com.doritech.microservice.ApiGateway.Filter;

import com.doritech.microservice.ApiGateway.Service.ApiGatewayLogService;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
@Order(1)
public class LoggingFilter implements GlobalFilter {

    private final ApiGatewayLogService logService;

    public LoggingFilter(ApiGatewayLogService logService) {
        this.logService = logService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String method = request.getMethod() != null ? request.getMethod().name() : "UNKNOWN";
        String path = request.getURI().getPath();
        String clientIp = request.getRemoteAddress() != null ? request.getRemoteAddress().getHostString() : "unknown";

        long startTime = System.currentTimeMillis();

        return chain.filter(exchange)
            .doOnSuccess(unused -> logRequestAsync(exchange, method, path, clientIp, startTime))
            .doOnError(throwable -> logRequestAsync(exchange, method, path, clientIp, startTime));
    }

    private void logRequestAsync(ServerWebExchange exchange, String method, String path, String clientIp, long startTime) {
        ServerHttpResponse response = exchange.getResponse();
        int statusCode = response.getStatusCode() != null ? response.getStatusCode().value() : HttpStatus.INTERNAL_SERVER_ERROR.value();
        long duration = System.currentTimeMillis() - startTime;

        Mono.fromRunnable(() -> logService.saveLog(method, path, clientIp, statusCode))
            .subscribeOn(Schedulers.boundedElastic())
            .subscribe();
    }
}
