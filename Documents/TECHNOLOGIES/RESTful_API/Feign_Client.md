## **2. Feign Client (из Spring Cloud OpenFeign)**

**Пакет:** `org.springframework.cloud:spring-cloud-starter-openfeign`  
**Тип:** Синхронный (но можно интегрировать с реактивными типами).  
**Плюсы:**  
✔ Декларативный стиль (интерфейсы + аннотации).  
✔ Интеграция с Spring Cloud (поддержка балансировки нагрузки, Circuit Breaker).  
✔ Автоматическая сериализация/десериализация.  
**Минусы:**  
❌ Требует настройки в Spring Cloud.

### Пример:

1. Включите Feign в приложении:
```java
@SpringBootApplication
@EnableFeignClients
public class MyApp { ... }
```

2. Объявите интерфейс:
```java
@FeignClient(name = "user-service", url = "https://jsonplaceholder.typicode.com")
public interface UserClient {
    @GetMapping("/users/{id}")
    User getUser(@PathVariable Long id);
}
```

3. Используйте в контроллере:
```java
@RestController
public class UserController {
    @Autowired
    private UserClient userClient;

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable Long id) {
        return userClient.getUser(id);
    }
}
```

---
