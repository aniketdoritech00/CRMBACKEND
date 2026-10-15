package com.doritech.microservice.ApiGateway.Service;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import com.doritech.microservice.ApiGateway.Entity.ApiGatewayLogEntity;
import com.doritech.microservice.ApiGateway.Repository.ApiGatewayLogRepository;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class ApiGatewayLogService {
    
    private final ApiGatewayLogRepository repository;
    
    public ApiGatewayLogService(ApiGatewayLogRepository repository) {
        this.repository = repository;
    }
    
    public void saveLog(String method, String path, String clientIp, int statusCode) {
        try {
            ApiGatewayLogEntity log = new ApiGatewayLogEntity(method, path, clientIp, statusCode, LocalDateTime.now());
            repository.save(log);
        } catch (Exception e) {
            System.err.println("Database logging error: " + e.getMessage());
        }
    }
    
    public Mono<Void> saveLogReactive(String method, String path, String clientIp, int statusCode) {
        return Mono.fromRunnable(() -> {
            ApiGatewayLogEntity log = new ApiGatewayLogEntity(method, path, clientIp, statusCode, LocalDateTime.now());
            repository.save(log);
        })
        .subscribeOn(Schedulers.boundedElastic()) 
        .doOnError(error -> System.err.println("Database logging error: " + error.getMessage()))
        .onErrorComplete() 
        .then();
    }
}