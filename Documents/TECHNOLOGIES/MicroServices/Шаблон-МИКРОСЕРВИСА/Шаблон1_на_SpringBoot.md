---
tags:
  - TECHNOLOGIES/Microsevices
  - TECHNOLOGIES/Microsevices/Шаблон
---
# Шаблон микросервиса на Spring Boot

## 1. Структура проекта
```text
microservice-template/
├── src/
│   ├── main/
│   │   ├── java/com/example/microservicetemplate/
│   │   │   ├── config/                # Конфигурационные классы
│   │   │   ├── controller/           # REST контроллеры
│   │   │   ├── dto/                  # Data Transfer Objects
│   │   │   ├── exception/            # Кастомные исключения
│   │   │   ├── model/                # Сущности/модели
│   │   │   ├── repository/           # Репозитории (JPA или другие)
│   │   │   ├── service/              # Бизнес-логика
│   │   │   ├── MicroserviceTemplateApplication.java
│   │   ├── resources/
│   │   │   ├── application.yml       # Конфигурация приложения
│   │   │   ├── bootstrap.yml        # Конфигурация для Spring Cloud Config (если нужно)
│   ├── test/                         # Тесты
├── pom.xml                           # или build.gradle
```

## 2. Основные файлы
### `MicroserviceTemplateApplication.java`
```java
package com.example.microservicetemplate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient // Если используете Service Discovery (Eureka, Consul и т.д.)
public class MicroserviceTemplateApplication {
    public static void main(String[] args) {
        SpringApplication.run(MicroserviceTemplateApplication.class, args);
    }
}
```

### Пример **контроллера** (`controller/ExampleController.java`)
```java
package com.example.microservicetemplate.controller;

import com.example.microservicetemplate.dto.ExampleDto;
import com.example.microservicetemplate.service.ExampleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/examples")
public class ExampleController {
    
    private final ExampleService exampleService;

    public ExampleController(ExampleService exampleService) {
        this.exampleService = exampleService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExampleDto> getExampleById(@PathVariable Long id) {
        return ResponseEntity.ok(exampleService.getExampleById(id));
    }

    @PostMapping
    public ResponseEntity<ExampleDto> createExample(@RequestBody ExampleDto exampleDto) {
        return ResponseEntity.ok(exampleService.createExample(exampleDto));
    }
}
```

### Пример **сервиса** (`service/ExampleService.java`)
```java
package com.example.microservicetemplate.service;

import com.example.microservicetemplate.dto.ExampleDto;
import com.example.microservicetemplate.model.Example;
import com.example.microservicetemplate.repository.ExampleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExampleService {
    
    private final ExampleRepository exampleRepository;

    public ExampleDto getExampleById(Long id) {
        Example example = exampleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Example not found"));
        return convertToDto(example);
    }

    public ExampleDto createExample(ExampleDto exampleDto) {
        Example example = convertToEntity(exampleDto);
        Example savedExample = exampleRepository.save(example);
        return convertToDto(savedExample);
    }

    private ExampleDto convertToDto(Example example) {
        // Реализация преобразования entity в DTO
    }

    private Example convertToEntity(ExampleDto exampleDto) {
        // Реализация преобразования DTO в entity
    }
}
```

### Пример **DTO** (`dto/ExampleDto.java`)
```java
package com.example.microservicetemplate.dto;

import lombok.Data;

@Data
public class ExampleDto {
    private Long id;
    private String name;
    private String description;
}
```

### `application.yml`
```yaml
server:
  port: 8080

spring:
  application:
    name: microservice-template
  datasource:
    url: jdbc:postgresql://localhost:5432/microservice_db
    username: postgres
    password: postgres
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

# Если используете Spring Cloud
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
```
## 3. `pom.xml` (основные зависимости)
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.0</version> <!-- Актуальная версия Spring Boot -->
        <relativePath/>
    </parent>
    
    <groupId>com.example</groupId>
    <artifactId>microservice-template</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>microservice-template</name>
    <description>Template for Spring Boot Microservice</description>

    <properties>
        <java.version>17</java.version>
    </properties>

    <dependencies>
        <!-- Spring Boot Starters -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        
        <!-- Spring Cloud (если нужно) -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        
        <!-- База данных -->
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <scope>runtime</scope>
        </dependency>
        
        <!-- Lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        
        <!-- Тестирование -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>2023.0.0</version> <!-- Актуальная версия Spring Cloud -->
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

## 4. Дополнительные рекомендации

1. **Документация API**: Добавьте Swagger/OpenAPI
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.3.0</version>
</dependency>
```
	   
2. **Логирование**: Настройте логирование через SLF4J + Logback
    
3. **Мониторинг**: Добавьте Spring Boot Actuator для мониторинга здоровья сервиса
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```
    
4. **Конфигурация**: Используйте Spring Cloud Config для централизованной конфигурации
    
5. **Безопасность**: Добавьте Spring Security для аутентификации/авторизации
    
6. **Распределенная трассировка**: Используйте Sleuth + Zipkin для трассировки запросов
    

Этот шаблон можно расширять в зависимости от конкретных требований вашего микросервиса.