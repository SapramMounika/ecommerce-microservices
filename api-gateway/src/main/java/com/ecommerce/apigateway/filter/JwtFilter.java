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

    // ===============================
    // 🔐 403 - ACCESS DENIED
    // ===============================
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

    // ===============================
    // 🔐 401 - UNAUTHORIZED
    // ===============================
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

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        System.out.println("JWT FILTER EXECUTED");

        String path = exchange.getRequest().getURI().getPath();

        // ✅ Allow auth APIs
        if (path.startsWith("/api/auth")) {
            return chain.filter(exchange);
        }

        // 🔐 Get Authorization header
        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return unauthorized(exchange, "Missing or invalid token");
        }

        String token = authHeader.substring(7);

        try {
            // 🔐 Validate token
            Claims claims = jwtUtil.validateAndGetClaims(token);

            String role = claims.get("role", String.class);
            String method = exchange.getRequest().getMethod().name();

            // ✅ Extract user info
            Object userIdObj = claims.get("userId");
            if (userIdObj == null) {
                return unauthorized(exchange, "Invalid token: userId missing");
            }

            String userId = userIdObj.toString();
            String username = claims.getSubject();

            // ✅ Add headers safely
            ServerWebExchange mutatedExchange = exchange.mutate()
                    .request(builder -> builder.headers(headers -> {
                        headers.remove("X-User-Id");
                        headers.remove("X-Username");
                        headers.remove("X-Role");

                        headers.add("X-User-Id", userId);
                        headers.add("X-Username", username);
                        headers.add("X-Role", role);
                    }))
                    .build();

            System.out.println("ROLE: " + role);
            System.out.println("PATH: " + path);
            System.out.println("METHOD: " + method);

            // ===============================
            // ✅ ALLOW USER TO PLACE ORDER
            // ===============================
            if (path.startsWith("/api/orders") && "POST".equals(method)) {
                if (!role.equals("USER") && !role.equals("ROLE_USER")) {
                    return accessDenied(exchange, "Only users can place orders");
                }
                return chain.filter(mutatedExchange);
            }

            // ===============================
            // 🔐 GET → USER + ADMIN
            // ===============================
            if ("GET".equals(method)) {
                if (!role.equals("USER") && !role.equals("ROLE_USER") &&
                    !role.equals("ADMIN") && !role.equals("ROLE_ADMIN")) {

                    return accessDenied(exchange, "Access Denied");
                }
            }

            // ===============================
            // 🔥 CART RULES
            // ===============================
            if (path.startsWith("/api/cart")) {

                if (path.equals("/api/cart/add") ||
                    path.equals("/api/cart/update") ||
                    path.equals("/api/cart/remove") ||
                    path.equals("/api/cart/view") ||
                    path.equals("/api/cart/clear")) {

                    if (role.equals("ADMIN") || role.equals("ROLE_ADMIN")) {
                        return accessDenied(exchange,
                                "Admin cannot perform user cart operations");
                    }
                }

                if (path.startsWith("/api/cart/admin")) {
                    if (role.equals("USER") || role.equals("ROLE_USER")) {
                        return accessDenied(exchange,
                                "User cannot access admin cart operations");
                    }
                }
            }
         // ===============================
         // 💳 PAYMENT RULES
            if (path.startsWith("/payments")) {

                if (!role.equals("USER") && !role.equals("ROLE_USER")) {
                    return accessDenied(exchange,
                            "Only users can make payments");
                }

                System.out.println("FORWARDING TO PAYMENT SERVICE");

                return chain.filter(mutatedExchange);
            }
         
      // ===============================
      // 🔥 INTERNAL SERVICE APIs
      // ===============================
      if (path.startsWith("/api/orders/internal")) {
          return chain.filter(mutatedExchange);
      }
            // ===============================
            // 🔒 OTHER SERVICES
            // ===============================
            if (("POST".equals(method) || "PUT".equals(method) || "DELETE".equals(method))
                    && !path.startsWith("/api/cart")) {

                if (!role.equals("ADMIN") && !role.equals("ROLE_ADMIN")) {
                    return accessDenied(exchange, "Access Denied");
                }
            }

            return chain.filter(mutatedExchange);

        } catch (Exception e) {

            e.printStackTrace();

            return unauthorized(exchange, e.getMessage());
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }
}