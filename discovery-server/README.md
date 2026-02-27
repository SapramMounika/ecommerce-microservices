# 🔎 Discovery Server (Eureka Server) – Documentation

---

## 1️. Overview

The Discovery Server is implemented using **Spring Cloud Netflix Eureka**.
It is responsible for service registration and discovery in the microservices architecture.
It acts as a **Service Registry** for the entire microservices architecture.
All microservices  register themselves with the Discovery Server so they can discover and communicate with each other dynamically.

---

## 2️. Why Do We Need a Discovery Server?

##### In microservices architecture:

- Each service runs on a different port.
- Services may scale dynamically.
- IP addresses and ports can change.

##### Without service discovery:

- Services must hardcode URLs.
- Scaling becomes difficult.
- System becomes tightly coupled.

##### With Eureka:

- Services register automatically.
- Services discover each other by name.
- No hardcoded URLs.
- Fully dynamic architecture.

---

## 3️. Technology Stack

- Java 17
- Spring Boot
- Spring Cloud Netflix Eureka
- Maven

---

## 4️. Architecture Role

### System Architecture Flow

Client

→ API Gateway  
→ Discovery Server (Service Lookup)  
→ Target Microservice  

---

## 5. How Service Registration Works

#### Step 1 – Discovery Server Starts

- Eureka Server runs on a defined port (e.g., 8761).
- It waits for services to register.

#### Step 2 – Microservice Starts

##### Example: User Service

 Reads configuration:

eureka.client.service-url.defaultZone=http://localhost:8761/eureka/

- Registers itself with:
- Service name
- Host
- Port
- Health status

#### Step 3 – Eureka Dashboard Updates

The service appears in:
```
http://localhost:8761
```
---
## 6. Project Configuration

#### 6.1 Dependencies (pom.xml)

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```
#### 6.2 Main Application Class

```java

@SpringBootApplication
@EnableEurekaServer
public class DiscoveryServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(DiscoveryServerApplication.class, args);
    }
}

```
#### 6.3 application.properties

```
server.port=8761

eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false

eureka.server.enable-self-preservation=true

```

### Property Explanation

| Property                      | Description                         |
| ----------------------------- | ----------------------------------- |
| register-with-eureka=false    | Server does not register itself     |
| fetch-registry=false          | Server does not fetch registry      |
| enable-self-preservation=true | Prevents accidental service removal |

## 7. Eureka Dashboard

Access:
```
http://localhost:8761
```

The dashboard displays:

- Registered services
- Instance count
- Status (UP / DOWN)
- Host and port
  
##### Example:

| Service Name | Instances |
| ------------ | --------- |
| USER-SERVICE | 1         |
| API-GATEWAY  | 1         |

## 8️. Service Communication Flow

##### Example:

API Gateway wants to call User Service.

Instead of calling:
```
http://localhost:8081
```
It calls:
```
http://USER-SERVICE
```
Eureka resolves:

- Service name → actual host & port
  
This allows dynamic scaling.

## 9️. Health Check Mechanism

Each service periodically sends a heartbeat to Eureka.

If heartbeat stops:

- Eureka marks service as DOWN.
- After threshold, removes instance.

Self-preservation mode prevents mass removal during network issues.

## 10. High Availability Considerations

In production:

- Run multiple Eureka servers.
- Configure peer replication.
- Deploy behind load balancer.

##### Example:
```
eureka.client.service-url.defaultZone=http://eureka1:8761/eureka/,http://eureka2:8761/eureka/
```

## 11. Scalability Benefits

- Services can scale horizontally.
- New instances auto-register.
- Load-balanced routing possible.
- No static configuration required.

## 1️2️. Security Considerations

In production:

- Do not expose Eureka dashboard publicly.
- Protect with basic auth.
- Restrict access via firewall.
- Use HTTPS.

## 1️3️. Failure Handling

If Discovery Server goes down:

- Already registered services continue working.
- New services cannot register.
- Communication may fail after restart if registry lost.

##### Solution:

- Use clustered Eureka setup.
- Use persistent storage if required.

## 1️4️. Integration with API Gateway

API Gateway uses:
```
spring.cloud.gateway.discovery.locator.enabled=true
```
This enables dynamic routing based on Eureka service names.

## 1️5️. Deployment Notes

- Runs as independent microservice.
- Should start before other services.
- Dockerize in production.
- Expose only internally.

## 1️6️. Advantages of Using Eureka

- Dynamic service discovery
- Loose coupling
- Horizontal scalability
- Load balancing ready
- Fault tolerance support

## 1️7️. Limitations

- Single node setup is not fault tolerant.
- Requires proper network configuration.
- Dashboard is not secured by default.

## 1️8️. Future Improvements

- Multi-zone clustering
- Secure with OAuth2
- Monitor via Prometheus
- Centralized logging integration

## 1️9️. Summary

The Discovery Server enables dynamic service registration and discovery within the microservices architecture.
It eliminates hardcoded service URLs and supports scalable, loosely coupled, and production-ready microservices communication.



