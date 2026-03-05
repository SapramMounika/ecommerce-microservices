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
    private Mono<Void> accessDenied(ServerWebExchange exchange) {

        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        exchange.getResponse().getHeaders().add("Content-Type", "application/json");

        String body = """
        {
          "message": "Access Denied"
        }
        """;

        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);

        return exchange.getResponse().writeWith(Mono.just(buffer));
        
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             GatewayFilterChain chain) {
    	System.out.println("JWT FILTER EXECUTED");
        String path = exchange.getRequest().getURI().getPath();

        // ✅ Allow auth endpoints without token
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

            // ✅ Validate token first
            Claims claims = jwtUtil.validateAndGetClaims(token);
            String role = claims.get("role", String.class);

            String method = exchange.getRequest().getMethod().name();

            // 🔐 Allow GET for USER + ADMIN
            if ("GET".equals(method)) {
                if (!"ROLE_USER".equals(role) &&
                    !"ROLE_ADMIN".equals(role)) {
                    return accessDenied(exchange);
                }
            }

            // 🔐 Allow ONLY ADMIN for write operations
            if ("POST".equals(method)
                    || "PUT".equals(method)
                    || "DELETE".equals(method)) {

                if (!"ROLE_ADMIN".equals(role)) {
                    return accessDenied(exchange);
                }
            }

        } catch (Exception e) {
            // ❌ Invalid token
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1; // Run before routing
    }
}