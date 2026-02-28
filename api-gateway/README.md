# 🚪 API Gateway – Enterprise Documentation

---

## 1️. Overview

The API Gateway acts as the **single entry point** for all client requests in the microservices architecture.
Instead of clients calling individual services directly, all requests flow through the API Gateway.

The gateway is responsible for:

- Centralized routing
- Request forwarding
- Load-balanced service resolution (via Eureka)
- JWT authentication validation (future-ready)
- Security enforcement
- Cross-cutting concerns (logging, monitoring, rate limiting)

---

## 2. Why API Gateway?

In microservices architecture:

##### Without Gateway:

- Clients must know all service URLs.
- Security must be implemented in each service.
- Routing logic is duplicated.
- Hard to manage scaling.

##### With Gateway:

- Single public endpoint.
- Centralized routing.
- Centralized security.
- Service abstraction.
- Simplified client integration.

---

## 3. Technology Stack

- Java 17
- Spring Boot
- Spring Cloud Gateway
- Eureka Client
- Maven

---

## 4️. Architecture Role

#### Request Flow

Client 

→ API Gateway  
→ Discovery Server (Service Lookup)  
→ Target Microservice  
→ Response returned via Gateway  

The Gateway resolves service names dynamically using Eureka.

---

## 5. Core Responsibilities

#### 5.1 Centralized Routing

Routes are configured to forward requests to services by name.

Example:
```
/api/users/** → USER-SERVICE
```

---

#### 5.2 Service Discovery Integration

Instead of hardcoded URLs:
```
http://localhost:8081
```
Gateway uses:
```
lb://USER-SERVICE
```

`lb://` means Load Balanced via Eureka.

---

#### 5.3 Security Enforcement

Gateway can:

- Validate JWT tokens
- Reject unauthorized requests
- Enforce authentication globally
- Prevent direct service access

---

#### 5.4 Cross-Cutting Concerns

Future-ready for:

- Logging
- Rate limiting
- CORS handling
- Header manipulation
- Request/Response transformation

---

## 6. Project Configuration

---

### 6.1 Dependencies (pom.xml)

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```
#### 6.2 application.properties
```
server.port=8080

spring.application.name=api-gateway

eureka.client.service-url.defaultZone=http://localhost:8761/eureka/

spring.cloud.gateway.discovery.locator.enabled=true
spring.cloud.gateway.discovery.locator.lower-case-service-id=true6.2 application.properties
```
## 7. Routing Configuration

Example static route configuration:

```java
spring.cloud.gateway.routes[0].id=user-service
spring.cloud.gateway.routes[0].uri=lb://USER-SERVICE
spring.cloud.gateway.routes[0].predicates[0]=Path=/api/users/**
```
Explanation:

| Property | Meaning                   |
| -------- | ------------------------- |
| id       | Route identifier          |
| uri      | Target service via Eureka |
| Path     | URL pattern to match      |

## 8. How Routing Works

 1. Client sends request to:
```
http://localhost:8080/api/users/viewAll
```
 2. Gateway matches path /api/users/**.

 3. Gateway forwards request to:
```
lb://USER-SERVICE
```
4. Eureka resolves actual instance.

5. Request reaches User Service.

6. Response is sent back through Gateway.

## 9️. Load Balancing

When multiple instances exist:

##### Example:

- USER-SERVICE instance 1 (8081)
- USER-SERVICE instance 2 (8082)

Gateway distributes traffic automatically using client-side load balancing.

## 10. Security Architecture

Currently:

- User Service validates JWT.

Future production improvement:

Move JWT validation to Gateway.

##### Advantages:

- Centralized authentication
- Microservices remain clean
- Reduced duplicate security logic

## 11. Direct Service Access (Development vs Production)

##### Development Mode

Services can be accessed directly:
```
http://localhost:8081/api/users/viewAll
```
##### Production Mode (Recommended)

- Direct service ports should be blocked.
- Only Gateway port exposed.
- Internal services communicate privately.

## 12. Error Handling

#### Gateway forwards:

* 401 Unauthorized
* 403 Forbidden
* 500 Internal Server Error

#### Can be extended to implement:

* Global error wrapper
* Standardized error responses

## 13. Scalability Considerations

- Stateless routing.
- Load-balanced resolution.
- Can scale horizontally.
- Supports reactive programming (non-blocking I/O).

## 14. Performance Considerations

- Avoid heavy processing inside Gateway.
- Keep routing lightweight.
- Use filters wisely.
- Enable compression if needed.

## 15. Production Hardening Recommendations

- Enable HTTPS.
- Protect Gateway with firewall.
- Add rate limiting.
- Implement request logging.
- Add monitoring (Prometheus, Grafana).
- Use environment-based configuration.

## 16. Future Enhancements

- Centralized JWT validation
- Global rate limiter
- Circuit breaker (Resilience4j)
- API versioning
- Request throttling
- Distributed tracing

## 17. Known Limitations

- No rate limiting implemented yet.
- No global JWT filter at Gateway level.
- No circuit breaker configured.
- No fallback routing.

## 18. Deployment Strategy

- Deploy as independent microservice.
- Should start after Discovery Server.
- Should run before exposing system externally.
- Expose only Gateway port publicly.
- Internal services remain private.

## 19. Summary

The API Gateway provides:

- Centralized request routing
- Service abstraction
- Load-balanced communication
- Security enforcement capability
- Scalable entry point for microservices

It is a critical component for achieving production-grade microservices architecture.





