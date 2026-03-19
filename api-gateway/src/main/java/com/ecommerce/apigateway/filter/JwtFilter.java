package com.ecommerce.apigateway.filter;

import java.nio.charset.StandardCharsets;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.ecommerce.apigateway.util.JwtUtil;

import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;

@Component
public class JwtFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    // 🔐 Custom 403 Response
    private Mono<Void> accessDenied(ServerWebExchange exchange, String message) {

        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        exchange.getResponse().getHeaders().add("Content-Type", "application/json");

        String body = """
        {
          "success": false,
          "message": "%s",
          "data": null,
          "status": 403,
          "timestamp": "%s"
        }
        """.formatted(message, java.time.LocalDateTime.now());

        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);

        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             GatewayFilterChain chain) {

        System.out.println("JWT FILTER EXECUTED");

        String path = exchange.getRequest().getURI().getPath();

        // ✅ Allow auth APIs
        if (path.startsWith("/api/auth")) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst("Authorization");

        // ❌ No token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        try {

            Claims claims = jwtUtil.validateAndGetClaims(token);
            String role = claims.get("role", String.class);

            String method = exchange.getRequest().getMethod().name();

            // ✅ DEBUG LOGS (CORRECT PLACE)
            System.out.println("TOKEN: " + token);
            System.out.println("ROLE: " + role);
            System.out.println("METHOD: " + method);

            // 🔐 GET → USER + ADMIN
            if ("GET".equals(method)) {
                if (!"ROLE_USER".equals(role) &&
                    !"ROLE_ADMIN".equals(role) &&
                    !"USER".equals(role) &&
                    !"ADMIN".equals(role)) {

                    return accessDenied(exchange, "Access Denied");
                }
            }

            // 🔐 WRITE → ADMIN ONLY
            if ("POST".equals(method)
                    || "PUT".equals(method)
                    || "DELETE".equals(method)) {

                if (!"ROLE_ADMIN".equals(role) &&
                    !"ADMIN".equals(role)) {

                    return accessDenied(exchange, "Access Denied");
                }
            }

        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
    
    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {

        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().add("Content-Type", "application/json");

        String body = """
        {
          "success": false,
          "message": "%s",
          "data": null,
          "status": 401,
          "timestamp": "%s"
        }
        """.formatted(message, java.time.LocalDateTime.now());

        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);

        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}