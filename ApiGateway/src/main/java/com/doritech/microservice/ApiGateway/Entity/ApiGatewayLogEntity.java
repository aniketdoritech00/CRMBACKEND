package com.doritech.microservice.ApiGateway.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "api_gateway_logs")
public class ApiGatewayLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String method;
    private String path;
    private String clientIp;
    private int statusCode;
    private LocalDateTime timestamp;

    public ApiGatewayLogEntity() {}

    public ApiGatewayLogEntity(String method, String path, String clientIp, int statusCode, LocalDateTime timestamp) {
        this.method = method;
        this.path = path;
        this.clientIp = clientIp;
        this.statusCode = statusCode;
        this.timestamp = timestamp;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public String getClientIp() { return clientIp; }
    public void setClientIp(String clientIp) { this.clientIp = clientIp; }

    public int getStatusCode() { return statusCode; }
    public void setStatusCode(int statusCode) { this.statusCode = statusCode; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
