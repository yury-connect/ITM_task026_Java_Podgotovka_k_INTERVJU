---
tags:
  - TECHNOLOGIES/Microsevices
  - TECHNOLOGIES/Microsevices/Шаблон
---
## Шаблон микросервиса на Spring Boot (User Service)

Ниже представлен улучшенный и расширенный шаблон микросервиса для управления пользователями. Включены лучшие практики:
- Чистая архитектура (слои Controller, Service, Repository)    
- Использование DTO и MapStruct для маппинга    
- Глобальная обработка ошибок    
- Валидация запросов    
- Swagger/OpenAPI документация    
- Аудит через spring-boot-starter-actuator    
- Dockerfile для контейнеризации    
---
### Структура проекта

```
user-service/
├── pom.xml
├── Dockerfile
├── src
│   ├── main
│   │   ├── java/com/example/userservice
│   │   │   ├── UserServiceApplication.java
│   │   │   ├── config
│   │   │   │   └── OpenApiConfig.java
│   │   │   ├── controller
│   │   │   │   └── UserController.java
│   │   │   ├── dto
│   │   │   │   └── UserDto.java
│   │   │   ├── entity
│   │   │   │   └── User.java
│   │   │   ├── exception
│   │   │   │   └── RestExceptionHandler.java
│   │   │   ├── mapper
│   │   │   │   └── UserMapper.java
│   │   │   ├── repository
│   │   │   │   └── UserRepository.java
│   │   │   └── service
│   │   │       └── UserService.java
│   │   └── resources
│   │       ├── application.yml
│   │       └── db
│   │           └── migration
│   │               └── V1__init.sql
│   └── test/java/com/example/userservice
│       └── UserServiceApplicationTests.java
└── README.md
```

---
### 1. `pom.xml`

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" ...>
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.example</groupId>
    <artifactId>user-service</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>

    <properties>
        <java.version>17</java.version>
        <spring.boot.version>3.1.0</spring.boot.version>
        <mapstruct.version>1.5.3.Final</mapstruct.version>
    </properties>

    <dependencies>
        <!-- Spring Boot Starter -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
        </dependency>
        <!-- Validation -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        <!-- MapStruct -->
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct</artifactId>
            <version>${mapstruct.version}</version>
        </dependency>
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct-processor</artifactId>
            <version>${mapstruct.version}</version>
            <scope>provided</scope>
        </dependency>
        <!-- OpenAPI/Swagger -->
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>2.1.0</version>
        </dependency>
        <!-- Actuator -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>io.micrometer</groupId>
            <artifactId>micrometer-registry-prometheus</artifactId>
        </dependency>
        <!-- Test -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>${java.version}</source>
                    <target>${java.version}</target>
                    <annotationProcessorPaths>
                        <path>
                            <groupId>org.mapstruct</groupId>
                            <artifactId>mapstruct-processor</artifactId>
                            <version>${mapstruct.version}</version>
                        </path>
                    </annotationProcessorPaths>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

---
### 2. `UserServiceApplication.java`

```java
package com.example.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
```

---
### 3. `entity/User.java`

```java
package com.example.userservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String username;

    @Column(nullable=false)
    private String email;

    @Column(nullable=false)
    private String password;

    @Column
    private Instant createdAt;

    @Column
    private Instant updatedAt;

    @PrePersist
    void onCreate() { createdAt = Instant.now(); }

    @PreUpdate
    void onUpdate() { updatedAt = Instant.now(); }
}
```

---
### 4. `dto/UserDto.java`

```java
package com.example.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserDto {
    private Long id;

    @NotBlank(message = "Username обязательено")
    private String username;

    @Email(message = "Неверный формат email")
    @NotBlank(message = "Email обязательено")
    private String email;

    @NotBlank(message = "Пароль обязательен")
    private String password;
}
```

---
### 5. `mapper/UserMapper.java`

```java
package com.example.userservice.mapper;

import com.example.userservice.entity.User;
import com.example.userservice.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(UserDto dto);
}
```

---
### 6. `repository/UserRepository.java`

```java
package com.example.userservice.repository;

import com.example.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
```

---
### 7. `service/UserService.java`

```java
package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.entity.User;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private final UserRepository repo;
    private final UserMapper mapper;

    public UserService(UserRepository repo, UserMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public UserDto create(UserDto dto) {
        User saved = repo.save(mapper.toEntity(dto));
        return mapper.toDto(saved);
    }

    public UserDto get(Long id) {
        User user = repo.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
        return mapper.toDto(user);
    }

    public List<UserDto> list() {
        return repo.findAll().stream()
            .map(mapper::toDto)
            .collect(Collectors.toList());
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
```

---
### 8. `controller/UserController.java`

```java
package com.example.userservice.controller;

import com.example.userservice.dto.UserDto;
import com.example.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;
    public UserController(UserService service) { this.service = service; }

    @Operation(summary = "Создать пользователя")
    @PostMapping
    public ResponseEntity<UserDto> create(@Validated @RequestBody UserDto dto) {
        return new ResponseEntity<>(service.create(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Получить пользователя по ID")
    @GetMapping("/{id}")
    public UserDto get(@PathVariable Long id) {
        return service.get(id);
    }

    @Operation(summary = "Список всех пользователей")
    @GetMapping
    public List<UserDto> list() {
        return service.list();
    }

    @Operation(summary = "Удалить пользователя")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
```

---
### 9. `exception/RestExceptionHandler.java`

```java
package com.example.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String,String> errors = ex.getBindingResult().getFieldErrors().stream()
            .collect(Collectors.toMap(
                err -> err.getField(),
                err -> err.getDefaultMessage()
            ));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntime(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

---
### 10. `config/OpenApiConfig.java`

```java
package com.example.userservice.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
            .info(new Info()
                .title("User Service API")
                .version("1.0")
                .description("API для управления пользователями"));
    }
}
```

---
### 11. `src/main/resources/application.yml`

```yaml
spring:
  datasource:
    url: ${DB_URL:jdbc:postgresql://localhost:5432/users}
    username: ${DB_USER:postgres}
    password: ${DB_PASS:password}
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        format_sql: true
management:
  endpoints:
    web:
      exposure:
        include: health,info,prometheus
```

---
### 12. `Dockerfile`

```dockerfile
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY target/user-service-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
```

---
### 13. `README.md`

````markdown
# User Service

Микросервис управления пользователями на Spring Boot

## Запуск

```bash
mvn clean package
docker build -t user-service .
docker run -p 8080:8080 user-service
````

Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) Actuator: [http://localhost:8080/actuator](http://localhost:8080/actuator) Prometheus: [http://localhost:8080/actuator/prometheus](http://localhost:8080/actuator/prometheus)

```
```


