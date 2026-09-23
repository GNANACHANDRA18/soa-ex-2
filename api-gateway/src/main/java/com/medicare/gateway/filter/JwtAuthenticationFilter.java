package com.medicare.gateway.filter;

import com.medicare.gateway.util.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Global JWT authentication filter applied at the API Gateway.
 * All requests routed through the gateway (except public auth endpoints
 * and eureka/actuator paths) must present a valid Bearer token.
 */
@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;

    // Paths that do not require authentication, as seen by the GATEWAY.
    // /auth/** is the explicit route; /auth-service/auth/** is the
    // discovery-locator route for the same endpoints.
    private static final List<String> OPEN_API_ENDPOINTS = List.of(
            "/auth/login",
            "/auth/health",
            "/auth-service/auth/login",
            "/auth-service/auth/health",
            "/actuator",
            "/eureka"
    );

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    private boolean isSecured(ServerHttpRequest request) {
        String path = request.getURI().getPath();
        return OPEN_API_ENDPOINTS.stream().noneMatch(path::startsWith);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (isSecured(request)) {
            if (!request.getHeaders().containsKey("Authorization")) {
                return onError(exchange, "Missing Authorization header");
            }

            String authHeader = request.getHeaders().getOrEmpty("Authorization").get(0);
            String token = authHeader;
            if (authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            }

            if (!jwtUtil.isTokenValid(token)) {
                return onError(exchange, "Invalid or expired JWT token");
            }
        }

        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", "application/json");
        byte[] bytes = ("{\"error\": \"" + message + "\"}").getBytes();
        return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}