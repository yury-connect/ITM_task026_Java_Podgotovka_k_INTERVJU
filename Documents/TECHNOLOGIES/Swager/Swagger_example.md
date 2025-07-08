---
tags:
  - TECHNOLOGIES/Swagger
---
#### Как подключить и использовать **Swagger** в **Spring Boot** ✅

---
## 🚀 Алгоритм подключения Swagger (Spring Boot + SpringDoc OpenAPI)
1. **📦 Добавить зависимость**    
    - Используем `springdoc-openapi` (актуальный инструмент для Swagger UI).        
2. **⚙️ Настроить конфигурацию (по желанию)**    
    - Указать базовую инфу: название, описание, версию API и т.д.        
3. **🔍 Добавить аннотации к контроллерам**    
    - Комментируем методы и параметры с помощью OpenAPI-аннотаций.        
4. **🌐 Открыть Swagger UI**    
    - Открыть в браузере: 
      `http://localhost:8080/swagger-ui.html` или `/swagger-ui/index.html`        

---
## 🧪 Пример: Подключение Swagger в Spring Boot

### 1️⃣ Зависимости (`pom.xml`)
```xml
<dependency>
  <groupId>org.springdoc</groupId>
  <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
  <version>2.3.0</version> <!-- актуальная на 2025 год -->
</dependency>
```

---
### 2️⃣ Опциональная конфигурация (метаинформация API)
```java
@OpenAPIDefinition(
    info = @Info(
        title = "My API",
        version = "1.0",
        description = "Документация REST API"
    )
)
@SpringBootApplication
public class MyApplication { ... }
```

---
### 3️⃣ Пример контроллера с аннотациями
```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Operation(summary = "Получить пользователя по ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getById(id);
    }
}
```

---
## 🧭 Где открывать Swagger UI?
👉 `http://localhost:8080/swagger-ui.html`  
	или  
👉 `http://localhost:8080/swagger-ui/index.html`

---
## 📝 Поддерживаемые аннотации

|Аннотация|Назначение|
|---|---|
|`@Operation`|Описание эндпоинта|
|`@ApiResponse`|Возможные ответы|
|`@Parameter`|Комментарии к параметрам запроса|
|`@Schema`|Описание моделей (DTO)|

---
