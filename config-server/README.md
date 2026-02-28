# ⚙️ Config Server – Enterprise Documentation


## 1. Overview

The Config Server is implemented using **Spring Cloud Config Server**.
It provides **centralized configuration management** for all microservices in the system.
Instead of each service maintaining its own configuration locally, configurations are stored centrally and fetched at runtime.


## 2. Why Config Server?

In microservices architecture:

##### Without Config Server:

- Each service has its own application.properties
- Configuration duplication
- Hard to maintain across environments
- Restart required for every configuration change
- Risk of inconsistency

##### With Config Server:

- Centralized configuration
- Environment-specific properties
- Easier scaling
- Clean separation of config from code
- Production-ready architecture


## 3. Technology Stack

- Java 17
- Spring Boot
- Spring Cloud Config Server
- Maven

## 4. Architecture Role

#### Configuration Flow

Microservice

→ Config Server  
→ Local Config Repository  
→ application-{service}.properties  

Services fetch configuration during startup.


## 5. Configuration Repository Structure

Example folder:
ecommerce-config-repo/

Inside:
```
user-service.properties
api-gateway.properties
product-service.properties
```

Each file contains service-specific configuration.



## 6. How Config Server Works

- 1️. Config Server starts.  
- 2️. Microservice starts.  
- 3️. Microservice contacts Config Server.  
- 4️. Config Server reads properties from config repo.  
- 5️. Configuration is injected into microservice.  



## 7. Project Setup

#### 7.1 Dependencies (pom.xml)

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-config-server</artifactId>
</dependency>
```
#### 7.2 Main Application Class

```java

@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }
}
```
#### 7.3 application.properties (Config Server)
```
server.port=8888

spring.application.name=config-server

spring.cloud.config.server.native.search-locations=file:///D:/End-to-End Project/ecommerce-config-repo
spring.profiles.active=native
```

## 8️. Service Configuration Example

Example: user-service.properties

```java

server.port=8081

spring.datasource.url=jdbc:postgresql://localhost:5432/userdb
spring.datasource.username=postgres
spring.datasource.password=admin

jwt.secret=yourVeryLongSecretKeyHere
jwt.expiration=3600000

```
## 9. How Microservices Connect to Config Server

Each microservice must include:

##### Dependency

```java
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId>
</dependency>

```
##### bootstrap.properties (or application.properties in newer versions)
spring.application.name=user-service
spring.config.import=optional:configserver:http://localhost:8888

## 10. Accessing Configurations

You can test config server using:
```
http://localhost:8888/user-service/default
```
This returns:

```json
{
  "name": "user-service",
  "profiles": ["default"],
  "propertySources": [...]
}

```
## 11. Environment Support

Config Server supports:

- default
- dev
- test
- prod

##### Example:
```
user-service-dev.properties
user-service-prod.properties
```
This enables environment-based configuration management.

## 1️2️. Benefits of Config Server

* Centralized configuration
* Environment separation
* Dynamic property loading
* Reduced duplication
* Clean DevOps integration
* Microservice scalability

## 13. Security Considerations

In production:

- Do not expose Config Server publicly.
- Secure with authentication.
- Store secrets in encrypted format.
- Use HTTPS.
- Restrict access via firewall.

## 14. Production Deployment Strategy

- Deploy Config Server before microservices.
- Config Server should start before services.
- Host config repository in Git (recommended for production).
- Use environment variables for sensitive values.

## 15. Failure Handling

If Config Server is down:

- Microservices may fail to start.
- Use retry mechanism.
- Consider high availability setup.
- Deploy multiple instances.

## 16. Native vs Git Mode

Currently using:
```
Native Mode (Local file system)
```
Production recommendation:
```
Git-based configuration repository
```
Benefits:

- Version control
- Audit history
- Rollback capability
- Team collaboration

## 17. Scalability Considerations

- Config Server is stateless.
- Can be scaled horizontally.
- Use load balancer for production.
- Cache configurations for performance.

## 18. Known Limitations

- No encryption enabled yet.
- No authentication on config server.
- Single node deployment.
- Local file-based config (not Git).

## 19. Future Improvements

- Move to Git-backed repository.
- Enable encryption support.
- Add authentication (OAuth2).
- High-availability cluster.
- Integrate with Vault for secrets.

## 20. Architecture Summary

Client

→ API Gateway

→ Microservice

→ Config Server (during startup)

→ Configuration Repository

Config Server ensures centralized and consistent configuration across the system.

## 21 Conclusion

The Config Server provides centralized, scalable, and environment-aware configuration management, enabling clean separation of configuration from code and aligning the architecture with enterprise microservices standards.
